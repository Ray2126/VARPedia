package gemmas.main;

public class Creation {
    String name;
    Integer number;

    public Creation(){
        this.name = "";
        this.number = 0;
    }

    public Creation(String name, int number){
        this.name = name;
        this.number = number;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public String getName(){
        return name;
    }

    public int getNumber(){
        return number;
    }
    
    @Override
    public String toString() {
    	return name;
    }
}
