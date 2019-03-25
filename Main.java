import org.json.JSONException;

import java.io.*;

public class Main {
    public static void main(String [] args) throws IOException, JSONException {
        String inName = "necromancer.dat";
        String outName = "compiled/" + inName;
        InputStream is = new FileInputStream(inName);
        Bundle inBundle = Bundle.read(is);
        inBundle.data.put("gold", 999999);
        OutputStream os = new FileOutputStream(outName);
        Bundle.write(inBundle, os);
        System.out.println(inBundle.toString());
    }
}
