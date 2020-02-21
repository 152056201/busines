package com.neuedu.busines.common;

public class Consts {
    public static final String USER="CURRENT_USER";
    public enum ProductStatusEnum{


        PRODUCT_ONLINE(1,"在售"),
        PRODUCT_OFFLINE(2,"下架"),
        PRODUCT_DELETE(3,"已删除")
        ;

        int status;
        String desc;
        ProductStatusEnum(int status,String desc){
            this.status=status;
            this.desc=desc;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }


    public enum CartProductCheckEnum{


        CHECKED(1,"已选中"),
        UNCHECKED(0,"未选中")

        ;

        private int  check;
        private String desc;
        CartProductCheckEnum(int check,String desc){
            this.check=check;
            this.desc=desc;
        }

        public int getCheck() {
            return check;
        }

        public void setCheck(int check) {
            this.check = check;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
