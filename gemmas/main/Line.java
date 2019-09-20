package gemmas.main;

public class Line {
    String text;
    Integer number;

    public Line(){
        this.text = "";
        this.number = 0;
    }

    public Line(String text, int number){
        this.text = text;
        this.number = number;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public String getText(){
        return text;
    }

    public int getNumber(){
        return number;
    }
}
