
package com.example.edistynytandroid.datatypes.weatherstation;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class _6 {

    @SerializedName("v")
    @Expose
    private int v;

    /**
     * No args constructor for use in serialization
     * 
     */
    public _6() {
    }

    /**
     * 
     * @param v
     */
    public _6(int v) {
        super();
        this.v = v;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_6 .class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("v");
        sb.append('=');
        sb.append(this.v);
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
