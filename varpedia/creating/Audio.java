package varpedia.creating;

public class Audio {
        String name;
        String number;

        public Audio(){
            this.name = "";
            this.number = "";
        }

        public Audio(String name, String number){
            this.name = name;
            this.number = number;
        }

        public void setName(String name){
            this.name = name;
        }

        public void setNumber(String number){

            this.number = number;
        }

        public String getName(){
            return name;
        }

        public String getNumber(){
            return number.substring(0, number.length()-4);
        }

        @Override
        public String toString() {
            return name;
        }
}
