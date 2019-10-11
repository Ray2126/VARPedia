package varpedia;

public class Creation {
	private String name;
    private Integer number;

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
