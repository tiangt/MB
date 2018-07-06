package com.whzl.mengbi.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmjoyInfo {

    private FaceBean face;

    public FaceBean getFace() {
        return face;
    }

    public void setFace(FaceBean face) {
        this.face = face;
    }

    public static class FaceBean {
        @SerializedName("public")
        private List<PublicBean> faceList;

        public List<PublicBean> getFaceList() {
            return faceList;
        }

        public void setPublicX(List<PublicBean> publicX) {
            this.faceList = publicX;
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
