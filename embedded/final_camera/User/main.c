#include "stm32f10x.h"
#include "./ov7725/bsp_ov7725.h"
#include "./lcd/bsp_ili9341_lcd.h"
#include "./led/bsp_led.h"
#include "./usart/bsp_usart.h"
#include "./systick/bsp_SysTick.h"
#include "./WIA/wildfire_Image_assistant.h"

// ע����λ��������
// �ο�bsp_usart.h�ļ��ĵ�18��

extern uint8_t Ov7725_vsync;
unsigned int Task_Delay[NumOfTask];
extern OV7725_MODE_PARAM cam_mode;

int main()
{
	uint8_t retry = 0;
	unsigned int my_c=0;

  // ��ʼ��
	USART_Config();
	LED_GPIO_Config();
	SysTick_Init();
	
	// ov7725 gpio ��ʼ��
	OV7725_GPIO_Config();
	// ov7725 �Ĵ���Ĭ�����ó�ʼ��
	while(OV7725_Init() != SUCCESS)
	{
		retry++;
		if(retry>5)
		{ LED_YELLOW; while(1); }
	}
	// ��������ͷ����������ģʽ
	OV7725_Special_Effect(cam_mode.effect);
	// ����ģʽ ���Ͷ� ���ն� �Աȶ�
	OV7725_Light_Mode(cam_mode.light_mode);
	OV7725_Color_Saturation(cam_mode.saturation);
	OV7725_Brightness(cam_mode.brightness);
	OV7725_Contrast(cam_mode.contrast);
	// ����Ч��
	OV7725_Special_Effect(cam_mode.effect);
	// ����ͼ�������ģʽ��С
	OV7725_Window_Set(cam_mode.cam_sx, cam_mode.cam_sy,
										cam_mode.cam_width, cam_mode.cam_height,
										cam_mode.QVGA_VGA);
	Ov7725_vsync = 0;
	
	// Һ����ʼ��
	ILI9341_Init();
	ILI9341_GramScan(3);
	LCD_SetFont(&Font8x16);
	LCD_SetColors(RED,BLACK);
  ILI9341_Clear(0,0,LCD_X_LENGTH,LCD_Y_LENGTH);	//��������ʾȫ��
	// ��ʾ�ַ���ʾ��
	ILI9341_DispStringLine_EN(LINE(0),"OV7725 initialize success!");

	LED_BLUE;
	my_c=0;
	while(my_c<2000000)
	{ my_c+=1; }
	my_c=0;
	LED_RGBOFF;

	while(1)
	{
		if(my_c<24)
		{
			// ����ͷ���㵽��������ʾ��Һ����
			while(Ov7725_vsync != 2){}
			FIFO_PREPARE;
			ImagDisp(cam_mode.lcd_sx,cam_mode.lcd_sy,
							 cam_mode.cam_width,cam_mode.cam_height);
			Ov7725_vsync = 0;
			my_c+=1;
		}
		else
		{
			my_c=422000;
			while(my_c>0){my_c--;}
			my_write_rgb_wincc(cam_mode.cam_width, cam_mode.cam_height);
			// cam_mode.cam_width=320 cam_mode.cam_height=240
			// ������һ�䷢�͵�������Ϊ320*240*2=153600 byte
			my_c=0;
		}
  }
}
