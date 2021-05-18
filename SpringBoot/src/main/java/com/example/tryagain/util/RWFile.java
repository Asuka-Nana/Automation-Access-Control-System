package com.example.tryagain.util;

import java.io.*;

public class RWFile {
    public static void RWimg  (String username){
        File file = new File("D:\\JavaFile\\tryagain\\src\\main\\resources\\static\\image\\def.png");
        String path = "D:\\data\\" + username + ".jpg";
      FileInputStream in = null;
        FileOutputStream out = null;
//        try{
//             is = new FileInputStream(file);
//             out = new FileOutputStream(path);
//             out.write(is.readAllBytes());
//        }catch (IOException e){
//            e.printStackTrace();
//        }finally {
//            try {
//                // 关闭inputStream流
//                is.close();
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
        File originalFile = new File("D:\\JavaFile\\tryagain\\src\\main\\resources\\static\\image\\def.png");//指定要读取的图片
        try {
            File result = new File(path);//要写入的图片
            if (result.exists()) {//校验该文件是否已存在
                result.delete();//删除对应的文件，从磁盘中删除
                result = new File(path);//只是创建了一个File对象，并没有在磁盘下创建文件
            }
            if (!result.exists()) {//如果文件不存在
                result.createNewFile();//会在磁盘下创建文件，但此时大小为0K
            }
            in = new FileInputStream(originalFile);
             out = new FileOutputStream(result);// 指定要写入的图片
            int n = 0;// 每次读取的字节长度
            byte[] bb = new byte[1024];// 存储每次读取的内容
            while ((n = in.read(bb)) != -1) {
                out.write(bb, 0, n);// 将读取的内容，写入到输出流当中
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //执行完以上后，磁盘下的该文件才完整，大小是实际大小
            try {
               // 关闭inputStream流
               in.close();
               out.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
        }


    }
}
