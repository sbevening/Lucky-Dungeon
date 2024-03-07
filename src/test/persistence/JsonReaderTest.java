package persistence;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {
    @Test
    void readerFileDoesNotExistText() {
        JsonReader reader = new JsonReader("./data/fakeFile.json");
        try {
            reader.generatePlayerAndEnemy();
            fail("Expected to throw IOException");
        } catch (IOException e) {
            // valid if reaches catch block
        }
    }
}
