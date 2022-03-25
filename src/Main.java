import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

public class Main {
    private static final String STORAGE = "C:\\Users\\vlad_\\Desktop\\ComputerStorage";
    private static final String DESTINATION = "C:\\Users\\vlad_\\Desktop\\MP3PlayerStorage";

    private static List<String> getFileNames (String path) {
        List<String> list = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            list = walk.filter(Files::isRegularFile)
                    .map(x -> x.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static void copy(String source) throws IOException {
        File sourceFile = new File(source);

        Path destinationDir = Paths.get(DESTINATION);
        Files.copy(sourceFile.toPath(), destinationDir.resolve(sourceFile.getName()), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void main(String[] args) {
        List<String> storageFiles = getFileNames(STORAGE);
        List<String> destinationFiles = getFileNames(DESTINATION);

        int i = 0;
        while(i < storageFiles.size()){
            String name = storageFiles.get(i);
            String match = destinationFiles.stream()
                    .filter(name::equals)
                    .findAny()
                    .orElse(null);

            if (match != null) {
                destinationFiles.remove(i);
                storageFiles.remove(match);
            } else {
                i++;
            }
        }

        for (String file : storageFiles) {
            try{
                copy(STORAGE +  "\\" + file);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        for (String file : destinationFiles){
            try{
                Files.delete(Paths.get(DESTINATION + "\\" + file));
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        destinationFiles.forEach(System.out::println);
    }
}
