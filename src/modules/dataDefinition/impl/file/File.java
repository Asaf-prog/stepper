package modules.dataDefinition.impl.file;
import java.nio.file.Path;
import java.nio.file.Paths;

public class File {
    String _path;
    //todo add more thing we need about file in our system

    public File(String path) {//default constructor
        this._path = path;
    }

    public String getPath() {
        return this._path;
    }
}