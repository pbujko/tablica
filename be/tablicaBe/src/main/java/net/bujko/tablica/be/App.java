package net.bujko.tablica.be;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import net.bujko.tablica.be.categs.AllNodes;
import net.bujko.tablica.be.categs.CategoryManager;
import net.bujko.tablica.be.categs.Node;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws JAXBException, FileNotFoundException, Exception {
        System.out.println("Unmarshall test!");

        JAXBContext context = JAXBContext.newInstance(
                "net.bujko.tablica.be.categs");
        Unmarshaller unmarshaller = context.createUnmarshaller();

        AllNodes allnodes = (AllNodes) unmarshaller.unmarshal(new FileReader("/Users/pbujko/NetBeansProjects/tablica/be/tablicaBe/src/test/resources/nodesData.xml"));
        List<Node> listOfNodes = allnodes.getNode();
        for(Node node : listOfNodes){
            System.out.println(node);        
        }
    }
}
