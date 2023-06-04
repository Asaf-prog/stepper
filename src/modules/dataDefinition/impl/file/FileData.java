package modules.dataDefinition.impl.file;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileData  implements Serializable {
    String name;
  //  Path _path;

    File file;
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }



    public FileData(String name) {
        this.name = name;
    }

    public FileData(String name,File file) {//default constructor
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public FileData(File file) {
      //  this._path = Paths.get(file.getPath());
        this.name = file.getName();
        this.file = file;
    }
    @Override
    public String toString() {
        return "name : "+name + " path : "+file.getPath();
    }
}