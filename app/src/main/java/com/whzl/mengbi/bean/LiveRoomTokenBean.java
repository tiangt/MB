package com.whzl.mengbi.bean;

import java.util.List;

public class LiveRoomTokenBean {

    private int code;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int userId;
        private int programId;
        private String token;
        private List<RoomServerListBean> roomServerList;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getProgramId() {
            return programId;
        }

        public void setProgramId(int programId) {
            this.programId = programId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<RoomServerListBean> getRoomServerList() {
            return roomServerList;
        }

        public void setRoomServerList(List<RoomServerListBean> roomServerList) {
            this.roomServerList = roomServerList;
        }

        public static class RoomServerListBean {

            private int programId;
            private int dataPort;
            private int policyPort;
            private String webSocketPort;
            private String serverDomain;

            public int getProgramId() {
                return programId;
            }

            public void setProgramId(int programId) {
                this.programId = programId;
            }

            public int getDataPort() {
                return dataPort;
            }

            public void setDataPort(int dataPort) {
                this.dataPort = dataPort;
            }

            public int getPolicyPort() {
                return policyPort;
            }

            public void setPolicyPort(int policyPort) {
                this.policyPort = policyPort;
            }

            public String getWebSocketPort() {
                return webSocketPort;
            }

            public void setWebSocketPort(String webSocketPort) {
                this.webSocketPort = webSocketPort;
            }

            public String getServerDomain() {
                return serverDomain;
            }

            public void setServerDomain(String serverDomain) {
                this.serverDomain = serverDomain;
            }
        }
    }
}
