package com.homecooked.karim.homecooked.model;



     public class Cooker {

         public String id;
         public String name;
         public String phone;
         public String available;
         public String about;
         public String url;
         public String address;





         public Cooker( String id,String url,String name,String phone , String available , String about,String address) {

             this.id=id;
             this.url = url;
             this.name = name;
             this.phone=phone;
             this.available=available;
             this.about=about;
             this.address=address;
         }



    public Cooker(){

    }


         public Cooker(String name){
             this.name = name;
         }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

         public String getPhone() {
             return phone;
         }

         public void setPhone(String phone) {
             this.phone = phone;
         }

         public String getAvailable() {
             return available;
         }

         public void setAvailable(String available) {
             this.available = available;
         }

         public String getAbout() {
             return about;
         }

         public void setAbout(String about) {
             this.about = about;
         }


         public String getAddress() {
             return address;
         }

         public void setAddress(String adress) {
             this.address = adress;
         }

         public String getId() {
             return id;
         }

         public void setId(String id) {
             this.id = id;
         }
     }
