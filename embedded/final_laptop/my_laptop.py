from my_s_pytorch_infer import *
import serial
import time
import numpy
import pyzbar.pyzbar as pyzbar
from PIL import Image, ImageEnhance
import pymysql
from sys import exit as sys_exit
import hashlib


def readQR(path):
    # 参数：图片路径
    # 返回值：bar_codes
    # 读取bar_codes方法如下，可能图片里有多个二维码
    # for barcode in bar_codes:
    #     barcodeData = barcode.data.decode("utf-8")
    #     print(barcodeData)
    img = Image.open(path)
    img = ImageEnhance.Brightness(img).enhance(1.5)  # 增加亮度1.5
    img = ImageEnhance.Sharpness(img).enhance(17.0)  # 锐利化17.0
    img = ImageEnhance.Contrast(img).enhance(4.0)  # 增加对比度4.0
    bar_codes = pyzbar.decode(img)
    return bar_codes


def save_image(data_l, path):
    data_b = data_l[0]
    image = []
    p = 0
    for i in range(240):
        image.append([])
        for j in range(320):
            red = data_b[p + 1] & 248
            green = ((data_b[p + 1] % 8) << 5) + ((data_b[p] >> 5) << 2)
            blue = (data_b[p] % 32) << 3
            image[-1].append([red, green, blue])
            p += 2
    image_array = numpy.array(numpy.uint8(image))
    image_to_save = Image.fromarray(image_array)
    image_to_save.save(path)
    return True


def main():
    # 部分代码与数据库有关 在不带数据库调试时应注释掉

    ser = serial.Serial("COM4", 921600, timeout=2)
    ser2 = serial.Serial("COM3", 115200, timeout=2)
    # 一旦上面这一句执行完成 就不能再按下开发板上的RESET

    db = pymysql.connect(host="192.168.43.152", user="new_root", password="root",
                         database="test", charset='utf8', port=3306)
    cursor = db.cursor()

    num_pic = 0
    r_list = ['戴了口罩', '没有戴口罩', '没有人', '人数大于1']
    # while True:
    while num_pic < 160:
        r_data = b''
        t = time.time()
        while len(r_data) < 153600:
            if ser.in_waiting:
                r_data += ser.read(ser.in_waiting)
        t = time.time() - t
        print(t, end='\t')
        str0 = './test/e' + str(num_pic) + '.jpg'
        save_result = save_image([r_data], str0)
        if not save_result:
            print("save image error!")
            sys_exit()
        r_now = s_inference(str0)
        print(r_list[r_now], num_pic)
        ser2.write(str(r_now).encode("utf-8"))
        num_pic += 1
        if r_now == 0:
            # 二维码部分代码
            while num_pic < 160:
                r_data = b''
                t = time.time()
                while len(r_data) < 153600:
                    if ser.in_waiting:
                        r_data += ser.read(ser.in_waiting)
                t = time.time() - t
                print(t, end='\t')
                str0 = './test/e' + str(num_pic) + '.jpg'
                save_result = save_image([r_data], str0)
                if not save_result:
                    print("save image error!")
                    sys_exit()
                barcodes = readQR(str0)
                if len(barcodes) != 1:
                    ser2.write('5'.encode("utf-8"))
                    print('二维码识别失败', num_pic)
                    num_pic += 1
                else:
                    user = barcodes[0][0].decode('utf-8')[64:]
                    code = barcodes[0][0].decode('utf-8')[:64]
                    strsql = "SELECT * from tb_user where username ='" + user + "'"
                    cursor.execute(strsql)
                    row = cursor.fetchall()
                    if (len(row) == 0):
                        print("数据库中不存在此用户！", num_pic)
                        num_pic += 1
                        ser2.write('6'.encode("utf-8"))
                        break
                    else:
                        s = time.localtime(time.time())
                        s1 = time.localtime(time.time() - 60)
                        year = str(s.tm_year)
                        mon = str(s.tm_mon)
                        mon1 = str(s1.tm_mon)
                        day = str(s.tm_mday)
                        day1 = str(s1.tm_mday)
                        hour = str(s.tm_hour)
                        hour1 = str(s.tm_hour)
                        min = str(s.tm_min)
                        min1 = str(s1.tm_min)
                        res = row[0][1] + year + mon + day + hour + min
                        res1 = row[0][1] + year + mon1 + day1 + hour1 + min1
                        m = hashlib.sha256()
                        m.update(res.encode('utf-8'))
                        m1 = hashlib.sha256()
                        m1.update(res1.encode('utf-8'))
                        if code == m.hexdigest() or code == m1.hexdigest():
                            nowtime = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())[0:10]
                            sql = "INSERT INTO attendance VALUES ('" + user + "','" + nowtime + "')"
                            cursor.execute(sql)
                            db.commit()
                            ser2.write('4'.encode("utf-8"))
                            print("识别成功！用户名：", user, num_pic)
                            num_pic += 1
                        else:
                            print("口令错误！", num_pic)
                            num_pic += 1
                            ser2.write('7'.encode("utf-8"))
                    break
    ser.close()
    ser2.close()
    db.close()
    print('end')


if __name__ == "__main__":
    main()
