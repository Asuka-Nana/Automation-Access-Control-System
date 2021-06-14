/**
  ******************************************************************************
  * @file    wildfire_image_assistant.c
  * @version V1.0
  * @date    2020-xx-xx
  * @brief   Ұ������ͷ���ֽӿ�
  ******************************************************************************
  * @attention
  *
  * ʵ��ƽ̨:Ұ�� F103-ָ���� STM32 ������ 
  * ��̳    :http://www.firebbs.cn
  * �Ա�    :https://fire-stm32.taobao.com
  *
  ******************************************************************************
  */ 
  
#include "./WIA/wildfire_image_assistant.h"
#include "./ov7725/bsp_ov7725.h"
#include "./led/bsp_led.h"
#include "./protocol/protocol.h"
#include "./sccb/bsp_sccb.h"

extern uint8_t Ov7725_vsync;

/**
 * @brief  ������� CRC-16.
 *         CRC�ļĴ���ֵ��rcr_init�������������Բ�һ�μ����������ݵĽ��
 *         ������һ֡ͼ����150KB���ڲ�����һ�η�����ô������ݣ���Ҫ�ֶμ��㣩
 * @param  *data:  Ҫ��������ݵ�����.
 * @param  length: ���ݵĴ�С
 * @return status: ����CRC.
 */
static uint16_t calc_crc_16(uint8_t *data, uint16_t length, uint16_t rcr_init)
{
  uint16_t crc = rcr_init;
  
  for(int n = 0; n < length; n++)
  {
    crc = data[n] ^ crc;
    
    for(int i = 0;i < 8;i++)
    {
      if(crc & 0x01)
      {
        crc = (crc >> 1) ^ 0xA001;
      }
      else
      {
        crc = crc >> 1;
      }
    }
  }
  
  return crc;    // �������ֽں͵��ֽ�λ��
}

/**
 * @brief  ��������ͼ���ʽ������λ��.
 * @param  type:   ͼ���ʽ.
 * @param  width:  ͼ����.
 * @param  height: ͼ��߶�.
 * @return void.
 */
void ack_read_wincc(uint16_t number, uint8_t val)
{
  uint16_t crc_16 = 0xFFFF;
  
  uint8_t packet_head[16] = {0x53, 0x5A, 0x48, 0x59,     // ��ͷ
                             0x00,                       // �豸��ַ
                             0x10, 0x00, 0x00, 0x00,     // ������ (10+1+2+2+2)                
                             0x11,                       // ����ͼ���ʽָ��
                             };
  
  packet_head[10] = 0;         // ִ��ok
  
  packet_head[11] = number & 0xFF;               // �Ĵ�����Ӧ��λ�������
  packet_head[12] = (number >> 8 ) & 0xFF;       // �Ĵ�����Ӧ��λ�������
                             
  packet_head[13] = val;       // �Ĵ���ֵ
                             
  crc_16 = calc_crc_16((uint8_t *)&packet_head, sizeof(packet_head) - 2, crc_16);    // �ֶμ���crc��16��У����, �����ͷ��
  
  packet_head[14] = (crc_16 >> 8) & 0x00FF;
  packet_head[15] = crc_16 & 0x00FF;
                             
  CAM_ASS_SEND_DATA((uint8_t *)&packet_head, sizeof(packet_head));
}

/**
 * @brief   ���Ĵ���
 * @param   addr���Ĵ�����ַ.
 * @return  status: 0:Ӧ��-1����Ӧ��.
 */
void read_rge(uint16_t number, uint8_t addr)
{
  uint8_t buff[1] = {0};
  
  if (SCCB_ReadByte(buff, 1, addr) == 1)
  {
    ack_read_wincc(number, buff[0]);
  }
}

/**
 * @brief   ���յ����ݴ���
 * @param   void
 * @return  status: 0:Ӧ��-1����Ӧ��.
 */
int8_t receiving_process(void)
{
  uint8_t frame_data[128];         // Ҫ�ܷ������֡
  uint16_t frame_len = 0;          // ֡����
  uint8_t cmd_type = CMD_NONE;     // ��������
  
  while(1)
  {
    cmd_type = protocol_frame_parse(frame_data, &frame_len);
    switch (cmd_type)
    {
      case CMD_NONE:
      {
        return -1;
      }
      
      /* д�Ĵ��� */
      case CMD_WRITE_REG:
      {
//        uint8_t dev_add = frame_data[4];        // �豸��ַ
        uint8_t reg_add = frame_data[10];    // �Ĵ�����ַ
        uint8_t reg_val = frame_data[11];    // �Ĵ���ֵ
        
        SCCB_WriteByte(reg_add, reg_val);
        break;
      }
      
      /* ���Ĵ��� */
      case CMD_READ_REG:
      {
//        uint8_t dev_add = frame_data[4];   // �豸��ַ
        uint16_t number = frame_data[10] | (frame_data[11] << 8);     // ���
        uint8_t reg_add = frame_data[12];    // �Ĵ�����ַ
        
        read_rge(number, reg_add);
        break;
      }
      
      /* ���յ�Ӧ���ź� */
      case CMD_ACK:
      {
        return 0;
      }

      default: 
        return -1;
    }
  }
}

/**
 * @brief  ��������ͼ���ʽ������λ��.
 * @param  type:   ͼ���ʽ.
 * @param  width:  ͼ����.
 * @param  height: ͼ��߶�.
 * @return void.
 */
void set_wincc_format(uint8_t addr, uint8_t type, uint16_t width, uint16_t height)
{
  uint16_t crc_16 = 0xFFFF;
  
  uint8_t packet_head[17] = {0x53, 0x5A, 0x48, 0x59,     // ��ͷ
                             0x00,                       // �豸��ַ
                             0x11, 0x00, 0x00, 0x00,     // ������ (10+1+2+2+2)                
                             0x01,                       // ����ͼ���ʽָ��
                             };
  
  packet_head[4] = addr;    // �޸��豸��ַ
  
  packet_head[10] = type;
                             
  packet_head[11] = width & 0xFF;
  packet_head[12] = width >> 8;
                             
  packet_head[13] = height & 0xFF;
  packet_head[14] = height >> 8;
                             
  crc_16 = calc_crc_16((uint8_t *)&packet_head, sizeof(packet_head) - 2, crc_16);    // �ֶμ���crc��16��У����, �����ͷ��
  
  packet_head[15] = (crc_16 >> 8) & 0x00FF;
  packet_head[16] = crc_16 & 0x00FF;
                             
  CAM_ASS_SEND_DATA((uint8_t *)&packet_head, sizeof(packet_head));
}

/**
 * @brief  ����ͼ�����ݰ�����λ��.
 * @param  addr:   �豸��ַ��0 or 1.
 * @param  width:  ͼ����.
 * @param  height: ͼ��߶�.
 * @return 0���ɹ���-1��ʧ��.
 */
int write_rgb_wincc(uint8_t addr, uint16_t width, uint16_t height) 
{
  uint16_t i, j; 
  uint16_t crc_16 = 0xFFFF;
	uint16_t Camera_Data[640];
  static uint8_t flag = 0;

  /* ����ͼ���ͷ*/
  packet_head_t packet_head =
  {
    .head = FRAME_HEADER,   // ��ͷ
    .addr = 0x00,           // �豸��ַ
    .len  = 0x00,           // ������
    .cmd  = CMD_PIC_DATA,   // ����ͼ�����ݰ�
  };                        
  
  if (flag)    /* ����һ����λ����ͼ���ʽ */
  {
    receiving_process();    // ����һ��ǰ����յ�������
    do{
      set_wincc_format(addr, PIC_FORMAT_RGB565, width, height);     // ��������ͼ���ʽָ��
      flag++;
      if (flag > 5)
        LED1_ON;
    }while(receiving_process() != 0);    // �ж��Ƿ��յ�Ӧ��
    
    flag = 0;
    LED1_OFF;
  }

  if (Ov7725_vsync == 2)    // �ɼ����
  {
    FIFO_PREPARE;  			/*FIFO׼��*/
    
    packet_head.addr = addr;    // �޸��豸��ַ
                      
    /* �޸İ����� */
    packet_head.len = 10 + width * height * 2 + 2;    // ���������

    /* ����ͷ */
    CAM_ASS_SEND_DATA((uint8_t *)&packet_head, sizeof(packet_head));
		// �ֶμ���crc��16��У����, �����ͷ��
    crc_16 = calc_crc_16((uint8_t *)&packet_head, sizeof(packet_head), crc_16);
    /* ����ͼ������ */
    for(i = 0; i < width; i++)
    {
      for(j = 0; j < height; j++)
      {
        READ_FIFO_PIXEL(Camera_Data[j]);		// ��FIFO����һ��rgb565���ص�Camera_Data����
      }
      CAM_ASS_SEND_DATA((uint8_t *)Camera_Data, j*2); 		// �ֶη�����������
    }
    /*����У������*/
    crc_16 = ((crc_16&0x00FF)<<8)|((crc_16&0xFF00)>>8);    //  �������ֽں͵��ֽ�λ��
    CAM_ASS_SEND_DATA((uint8_t *)&crc_16, 2);              // ��λ������ѡCRC-16Ҳ��Ҫ�����������ֽ�
    
    Ov7725_vsync = 0;		 // ��ʼ�´βɼ�
  }

  return 0;    // ���سɹ�
}


// �������write_rgb_wincc���ĵõ�
int my_write_rgb_wincc(uint16_t width, uint16_t height) 
{
  uint16_t i,j; 
	uint16_t Camera_Data[640];
  if (Ov7725_vsync == 2)
  {
    FIFO_PREPARE;
    for(i = 0; i < width; i++)
    {
      for(j = 0; j < height; j++)
      {
        READ_FIFO_PIXEL(Camera_Data[j]);// ��FIFO����һ��rgb565���ص�Camera_Data����
      }
      CAM_ASS_SEND_DATA((uint8_t *)Camera_Data, j*2);// �ֶη�����������
    }
    Ov7725_vsync = 0;
  }
  return 0;
}
