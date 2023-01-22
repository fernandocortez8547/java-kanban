package localhost;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import static localhost.HttpTaskServer.FORMATTER;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(final JsonWriter writer, LocalDateTime time) {
        try {
            writer.value(time.format(FORMATTER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LocalDateTime read(final JsonReader reader) throws IOException{
//        LocalDateTime time = null;
//        try {
            return LocalDateTime.parse(reader.nextString(), FORMATTER);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return time;
    }
}
