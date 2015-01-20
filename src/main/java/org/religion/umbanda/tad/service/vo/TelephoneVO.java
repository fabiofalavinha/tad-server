package org.religion.umbanda.tad.service.vo;

import org.religion.umbanda.tad.model.PhoneType;

public class TelephoneVO {

    private PhoneType phoneType;
    private int areaCode;
    private int number;

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }
}
