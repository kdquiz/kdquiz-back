package kdquiz.configDto;

public class SecurityExceptionDto {
    private int statusCode;
    private String msg;

    public SecurityExceptionDto(int statusCode, String msg){
        this.statusCode = statusCode;
        this.msg =msg;
    }
}
