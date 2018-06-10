package cz.zcu.kiv.nlp.ir.trec.core;

import cz.zcu.kiv.nlp.ir.trec.data.DocumentNew;
import cz.zcu.kiv.nlp.ir.trec.data.Topic;
import cz.zcu.kiv.nlp.ir.trec.domain.Document;
import cz.zcu.kiv.nlp.ir.trec.domain.IDocument;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tigi
 */
public class SerializedDataHelper {

    static Logger log = Logger.getLogger(SerializedDataHelper.class);


    public static final java.text.DateFormat SDF = new SimpleDateFormat("yyyy-MM-dd_HH_mm_SS");

    static public List<IDocument> loadDocument(File serializedFile) {
        List<IDocument> rs = new ArrayList<>();

        final Object object;
        try {
            final ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(serializedFile));
            object = objectInputStream.readObject();
            objectInputStream.close();
            List map = (List) object;
            if (!map.isEmpty()) {

                for(Object o : map) {
                    Document d = new Document();
                    d.setTitle(((DocumentNew) o).getTitle());
                    d.setText(((DocumentNew) o).getText());
                    d.setDate(((DocumentNew) o).getDate());
                    d.setUrl(((DocumentNew) o).getId());
                    rs.add(d);
                }

                //return (List<IDocument>) object;
                return rs;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }


    public static void saveDocument(File outputFile, List<IDocument> data) {
        // save data
        try {
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(outputFile));
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info(Thread.currentThread().getName() + ": Data saved to " + outputFile.getPath());
    }

    static public List<Topic> loadTopic(File serializedFile) {
        final Object object;
        try {
            final ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(serializedFile));
            object = objectInputStream.readObject();
            objectInputStream.close();
            List map = (List) object;
            if (!map.isEmpty() && map.get(0) instanceof Topic) {
                return (List<Topic>) object;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    public static void saveTopic(File outputFile, List<Topic> data) {
        // save data
        try {
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(outputFile));
            objectOutputStream.writeObject(data);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info(Thread.currentThread().getName() + ": Data saved to " + outputFile.getPath());
    }
}
