package me.mamiiblt.instafel.utils.models;

public class DeviceData {

    private Object aver, sdk, model, brand, product;

    public DeviceData(Object aver, Object sdk, Object model, Object brand, Object product) {
        this.aver = aver;
        this.sdk = sdk;
        this.model = model;
        this.brand = brand;
        this.product = product;
    }

    public Object getAver() {
        return aver;
    }

    public Object getSdk() {
        return sdk;
    }

    public Object getModel() {
        return model;
    }

    public Object getBrand() {
        return brand;
    }

    public Object getProduct() {
        return product;
    }
}
