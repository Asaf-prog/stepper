package modules.dataDefinition.impl.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileData {
    String name;
    Path _path;

    File file;
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }



    //todo add more thing we need about file in our system

    public FileData(String name) {
        this.name = name;
    }

    public FileData(String name,File file) {//default constructor
        this.name = name;
        this._path = Paths.get(file.getPath());
        this.file = file;
    }

    public Path getPath() {
        return this._path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set_path(String _path) {
        this._path = Paths.get(_path);
    }

    public FileData(File file) {
        this._path = Paths.get(file.getPath());
        this.name = file.getName();
        this.file = file;
    }
}