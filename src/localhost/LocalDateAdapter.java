package localhost;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import util.FileConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import static localhost.handlers.HttpTaskServer.HTTP_SERVER_FORMATTER;

public class LocalDateAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(final JsonWriter writer, LocalDateTime time) {
        try {
            writer.value(time.format(HTTP_SERVER_FORMATTER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LocalDateTime read(final JsonReader reader) throws IOException{
            return LocalDateTime.parse(reader.nextString(), HTTP_SERVER_FORMATTER);
    }
}
