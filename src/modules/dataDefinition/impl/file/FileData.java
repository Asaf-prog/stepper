package modules.dataDefinition.impl.file;

import java.io.File;

public class FileData {
    String name;
    String _path;

    //todo add more thing we need about file in our system

    public FileData(String path) {
        this._path = path;
    }

    public FileData(String name, String path) {//default constructor
        this.name = name;
        this._path = path;
    }

    public String getPath() {
        return this._path;
    }

    public FileData(File file) {
        this._path = file.getPath();
        this.name = file.getName();
    }
}