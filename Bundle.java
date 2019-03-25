import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Bundle {
    JSONObject data;

    public Bundle() {
        this(new JSONObject());
    }

    private Bundle(JSONObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    private static final int GZIP_BUFFER_SIZE = 4096;

    static Bundle read(InputStream stream) throws IOException, JSONException {
        BufferedReader reader;
        PushbackInputStream pb = new PushbackInputStream(stream, 2);
        byte[] header = new byte[2];
        pb.unread(header, 0, pb.read(header));
        if (header[0] == (byte) 0x1f && header[1] == (byte) 0x8b) {
            reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(pb, GZIP_BUFFER_SIZE)));
        } else {
            reader = new BufferedReader(new InputStreamReader(pb));
        }
        JSONObject json = (JSONObject) new JSONTokener(reader.readLine()).nextValue();
        reader.close();
        return new Bundle(json);
    }

    static void write(Bundle bundle, OutputStream stream) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(stream, GZIP_BUFFER_SIZE)));
        writer.write(bundle.data.toString());
        writer.close();
    }
}
