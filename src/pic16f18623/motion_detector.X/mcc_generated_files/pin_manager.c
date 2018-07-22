/**
  Generated Pin Manager File

  Company:
    Microchip Technology Inc.

  File Name:
    pin_manager.c

  Summary:
    This is the Pin Manager file generated using PIC10 / PIC12 / PIC16 / PIC18 MCUs

  Description:
    This header file provides implementations for pin APIs for all pins selected in the GUI.
    Generation Information :
        Product Revision  :  PIC10 / PIC12 / PIC16 / PIC18 MCUs - 1.65.2
        Device            :  PIC16F18326
        Driver Version    :  2.01
    The generated drivers are tested against the following:
        Compiler          :  XC8 1.45
        MPLAB             :  MPLAB X 4.15

    Copyright (c) 2013 - 2015 released Microchip Technology Inc.  All rights reserved.
*/

/*
    (c) 2018 Microchip Technology Inc. and its subsidiaries. 
    
    Subject to your compliance with these terms, you may use Microchip software and any 
    derivatives exclusively with Microchip products. It is your responsibility to comply with third party 
    license terms applicable to your use of third party software (including open source software) that 
    may accompany Microchip software.
    
    THIS SOFTWARE IS SUPPLIED BY MICROCHIP "AS IS". NO WARRANTIES, WHETHER 
    EXPRESS, IMPLIED OR STATUTORY, APPLY TO THIS SOFTWARE, INCLUDING ANY 
    IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY, AND FITNESS 
    FOR A PARTICULAR PURPOSE.
    
    IN NO EVENT WILL MICROCHIP BE LIABLE FOR ANY INDIRECT, SPECIAL, PUNITIVE, 
    INCIDENTAL OR CONSEQUENTIAL LOSS, DAMAGE, COST OR EXPENSE OF ANY KIND 
    WHATSOEVER RELATED TO THE SOFTWARE, HOWEVER CAUSED, EVEN IF MICROCHIP 
    HAS BEEN ADVISED OF THE POSSIBILITY OR THE DAMAGES ARE FORESEEABLE. TO 
    THE FULLEST EXTENT ALLOWED BY LAW, MICROCHIP'S TOTAL LIABILITY ON ALL 
    CLAIMS IN ANY WAY RELATED TO THIS SOFTWARE WILL NOT EXCEED THE AMOUNT 
    OF FEES, IF ANY, THAT YOU HAVE PAID DIRECTLY TO MICROCHIP FOR THIS 
    SOFTWARE.
*/

#include <xc.h>
#include "pin_manager.h"
#include "stdbool.h"





void PIN_MANAGER_Initialize(void)
{
    /**
    LATx registers
    */
    LATA = 0x00;
    LATC = 0x00;

    /**
    TRISx registers
    */
    TRISA = 0x30;
    TRISC = 0x27;

    /**
    ANSELx registers
    */
    ANSELC = 0x00;
    ANSELA = 0x00;

    /**
    WPUx registers
    */
    WPUA = 0x00;
    WPUC = 0x00;

    /**
    ODx registers
    */
    ODCONA = 0x00;
    ODCONC = 0x00;





   
    
	
    RC1PPSbits.RC1PPS = 0x19;   //RC1->MSSP1:SDA1;    
    SSP1CLKPPSbits.SSP1CLKPPS = 0x10;   //RC0->MSSP1:SCL1;    
    SSP1DATPPSbits.SSP1DATPPS = 0x11;   //RC1->MSSP1:SDA1;    
    RXPPSbits.RXPPS = 0x15;   //RC5->EUSART:RX;    
    RA4PPSbits.RA4PPS = 0x1B;   //RA4->MSSP2:SDA2;    
    RC4PPSbits.RC4PPS = 0x14;   //RC4->EUSART:TX;    
    RC0PPSbits.RC0PPS = 0x18;   //RC0->MSSP1:SCL1;    
    SSP2DATPPSbits.SSP2DATPPS = 0x04;   //RA4->MSSP2:SDA2;    
    SSP2CLKPPSbits.SSP2CLKPPS = 0x05;   //RA5->MSSP2:SCL2;    
    RA5PPSbits.RA5PPS = 0x1A;   //RA5->MSSP2:SCL2;    
}
  
void PIN_MANAGER_IOC(void)
{   
}

/**
 End of File
*/