package TwoLevelCache;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemCache<KEY, VALUE extends Serializable> implements Cache<KEY,VALUE> {
    private Map<KEY, String> CacheObjects;
    private Path FileSystemCacheDir;
    private int MaxSize;

    FileSystemCache(int size) {
        try {
            this.FileSystemCacheDir = Files.createTempDirectory("FileSystemCache");
            this.FileSystemCacheDir.toFile().deleteOnExit();
        } catch (IOException ex) {
            System.out.println("Ошибка создание времееной директории");
            ex.printStackTrace();
        }
        this.CacheObjects = new HashMap<KEY, String>(size);
        this.MaxSize = size;
    }

    @Override
    public void put(KEY key, VALUE value) {

        try {
            File TmpFile = Files.createTempFile(FileSystemCacheDir,"","").toFile();
            ObjectOutputStream WriteStream = new ObjectOutputStream(new FileOutputStream(TmpFile));
            WriteStream.writeObject(value);
            WriteStream.flush();
            WriteStream.close();
            CacheObjects.put(key, TmpFile.getName());
            TmpFile.deleteOnExit();
        }
        catch (IOException ex) {
            System.out.println("Ошибка записи объекта в кеша файловой системы");
            ex.printStackTrace();
        }
    }

    @Override
    public VALUE get(KEY key){
        if (ContainsKey(key)) {
            String FileName = CacheObjects.get(key);
            try {
                FileInputStream InputFile = new FileInputStream(new File(FileSystemCacheDir + File.separator + FileName));
                ObjectInputStream ReadStream = new ObjectInputStream(InputFile);
                VALUE ReturnValue = (VALUE) ReadStream.readObject();
                ReadStream.close();
                return ReturnValue;
            }
            catch (IOException ex){
                System.out.println("Ошибка чтения объекта из кеша файловой системы ");
                ex.printStackTrace();
            }
            catch (ClassNotFoundException ex){
                ex.printStackTrace();
            }
        }

        System.out.println("Объект не найден");
        return null;
    }

    @Override
    public void remove(KEY key){
        String FileName = CacheObjects.get(key);
        File RemoveFile = new File(FileSystemCacheDir + File.separator + FileName);
        if (RemoveFile.delete()) {
            System.out.println("Объект удалён из кеша файловой системы");
        } else {
            System.out.println("Ошибка при удаление объекта из кеша файловой системы");
        }
        CacheObjects.remove(key);
    }

    @Override
    public void clear(){
        for (Map.Entry<KEY, String> entry : CacheObjects.entrySet()) {
            String FileName = CacheObjects.get(entry.getKey());
            File RemoveFile = new File(FileSystemCacheDir + File.separator + FileName);
            RemoveFile.delete();
        }
//        FileSystemCacheDir.toFile().delete();
        CacheObjects.clear();
    }

    @Override
    public int size(){
        return CacheObjects.size();
    }

    @Override
    public boolean ContainsKey(KEY key){
        return CacheObjects.containsKey(key);
    }

    @Override
    public boolean IsFreeSpace() {
        return size() < MaxSize;
    }
}
