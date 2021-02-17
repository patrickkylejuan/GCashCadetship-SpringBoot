package ph.apper.blogcreationapi.Classes;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class Blog {
    private String id;

    public Blog(String id) {
        this.id = id;
    }

    private String title;
    private String content;
    private String datePublished;
    private String author;

//    @JsonValue
//    public String info(){
//        return this.id + ":" + "title";}


}
