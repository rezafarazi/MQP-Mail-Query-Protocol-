package Http.Models;

public class FileResponseModel
{

    String StatusCode;
    String ContentType;
    String Path;
    byte []Content;

    public FileResponseModel()
    {
    }

    public FileResponseModel(String statusCode, String contentType, byte []content,String path)
    {
        StatusCode = statusCode;
        ContentType = contentType;
        Content = content;
        Path = path;
    }

    public String getStatusCode()
    {
        return StatusCode;
    }

    public void setStatusCode(String statusCode)
    {
        StatusCode = statusCode;
    }

    public String getContentType()
    {
        return ContentType;
    }

    public void setContentType(String contentType)
    {
        ContentType = contentType;
    }

    public byte[] getContent()
    {
        return Content;
    }

    public void setContent(byte []content)
    {
        Content = content;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

}
