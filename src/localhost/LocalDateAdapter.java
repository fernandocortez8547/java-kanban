package localhost;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import util.FileConverter;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(final JsonWriter writer, LocalDateTime time) {
        try {
            writer.value(time.format(FileConverter.HTTP_SERVER_FORMATTER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LocalDateTime read(final JsonReader reader) throws IOException{
//        LocalDateTime time = null;
//        try {
            return LocalDateTime.parse(reader.nextString(), FileConverter.HTTP_SERVER_FORMATTER);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return time;
    }
}
