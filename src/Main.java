import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

public class Main {
    private static final String storage = "C:\\Users\\vlad_\\Desktop\\ComputerStorage";
    private static final String destination = "C:\\Users\\vlad_\\Desktop\\MP3PlayerStorage";

    private static List<String> getFileNames (String path) {
        List<String> list = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            list = walk.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static void copy(String source) throws IOException {
        File sourceFile = new File(source);

        Path destinationDir = Paths.get(destination);
        Files.copy(sourceFile.toPath(), destinationDir.resolve(sourceFile.getName()), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void main(String[] args) {
        List<String> storageFiles = getFileNames(storage);
        List<String> destinationFiles = getFileNames(destination);

        for(String name : storageFiles){
            String match = destinationFiles.stream()
                    .filter(name::equals)
                    .findAny()
                    .orElse(null);

            if (match != null) {
                destinationFiles.remove(match);
                storageFiles.remove(match);
            }
        }

        for (String file : storageFiles) {
            try{
                copy(file);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        for (String file : destinationFiles){
            try{
                Files.delete(Paths.get(file));
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        destinationFiles.forEach(System.out::println);
    }
}
