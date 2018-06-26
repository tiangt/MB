package com.whzl.mengbi.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmjoyBean {

    private FaceBean face;

    public FaceBean getFace() {
        return face;
    }

    public void setFace(FaceBean face) {
        this.face = face;
    }

    public static class FaceBean {
        @SerializedName("public")
        private List<PublicBean> publicX;

        public List<PublicBean> getPublicX() {
            return publicX;
        }

        public void setPublicX(List<PublicBean> publicX) {
            this.publicX = publicX;
        }

        public static class PublicBean {

            private String value;
            private String icon;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}
