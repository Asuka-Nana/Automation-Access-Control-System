#include "stm32f10x.h"
#include "./lcd/bsp_ili9341_lcd.h"
#include "./led/bsp_led.h" 
#include "./usart/bsp_usart.h"

int main()
{
  char ch;

	// Һ����ʼ��
	ILI9341_Init();
	ILI9341_GramScan(3);
	LCD_SetFont(&Font8x16);
	LCD_SetColors(RED,BLACK);
  ILI9341_Clear(0,0,LCD_X_LENGTH,LCD_Y_LENGTH);// ����
	// ��ʾ�ַ���ʾ��
	ILI9341_DispStringLine_EN(LINE(0),"OV7725 initialize success!");
	
	USART_Config();
	LED_GPIO_Config();
	
  while(1)
	{
    ch=getchar();
    switch(ch)
    {
			case '0':// ���˿��� ��Ҫ��ʾ��ά��
        LED_YELLOW; ILI9341_Clear(0,0,LCD_X_LENGTH,LCD_Y_LENGTH);
			  ILI9341_DispStringLine_EN(LINE(0),"Please show your QR code."); break;
      case '1':// û������
        LED_RED; ILI9341_Clear(0,0,LCD_X_LENGTH,LCD_Y_LENGTH);
			  ILI9341_DispStringLine_EN(LINE(0),"Please put on your mask!"); break;
      case '2':// û����
        LED_BLUE; ILI9341_Clear(0,0,LCD_X_LENGTH,LCD_Y_LENGTH);
			  ILI9341_DispStringLine_EN(LINE(0),"..."); break;
      case '3':// ��������1
        LED_PURPLE; ILI9341_Clear(0,0,LCD_X_LENGTH,LCD_Y_LENGTH);
			  ILI9341_DispStringLine_EN(LINE(0),"More than one person!"); break;
      case '4':// ��ά��ʶ��ɹ�
        LED_GREEN; ILI9341_Clear(0,0,LCD_X_LENGTH,LCD_Y_LENGTH);
			  ILI9341_DispStringLine_EN(LINE(0),"QR code identified successfully. You can enter."); break;
      case '5':// ��ά��ʶ��ʧ��
        LED_RED; ILI9341_Clear(0,0,LCD_X_LENGTH,LCD_Y_LENGTH);
			  ILI9341_DispStringLine_EN(LINE(0),"Failed to identify QR code!"); break;
			case '6':// ���ݿ��в����ڸ��û�
        LED_RED; ILI9341_Clear(0,0,LCD_X_LENGTH,LCD_Y_LENGTH);
			  ILI9341_DispStringLine_EN(LINE(0),"Can't find the user in the database!"); break;
			case '7':// �������
        LED_RED; ILI9341_Clear(0,0,LCD_X_LENGTH,LCD_Y_LENGTH);
			  ILI9341_DispStringLine_EN(LINE(0),"Wrong password!"); break;
      default:
        LED_RGBOFF; ILI9341_Clear(0,0,LCD_X_LENGTH,LCD_Y_LENGTH); break;
    }
	}
}
