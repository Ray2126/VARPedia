package varpedia;

public class Creation {
	private String name;
    private Integer number;
    private String search;

    public Creation(){
        this("",0,"");
    }

    public Creation(String name, int number, String search){
        this.name = name;
        this.number = number;
        this.search = search;
    }
    
	public void setName(String name){
        this.name = name;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public void setSearch(String search){
        this.search = search;
    }

    public String getSearch(){
        return search;
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
