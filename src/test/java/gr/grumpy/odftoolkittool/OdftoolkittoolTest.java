package gr.grumpy.odftoolkittool;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.odftoolkit.odfdom.dom.OdfSchemaDocument;
import org.odftoolkit.odfdom.dom.attribute.fo.FoKeepTogetherAttribute;
import org.odftoolkit.odfdom.dom.attribute.style.StyleMayBreakBetweenRowsAttribute;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;

public class OdftoolkittoolTest {

    @org.junit.jupiter.api.Test
    public void testMain() throws Exception {
    
        InputStream is = OdftoolkittoolTest.class.getClassLoader().getResourceAsStream("test.odt");
        TextDocument textDocument = TextDocument.loadDocument(is);

        Table tbl = textDocument.getTableByName("Table2");
        if (tbl == null) {
            tbl = Table.newTable(textDocument);
        }
        
        OdfStyle odfStyle = tbl.getStyleHandler().getStyleElementForWrite();
        
        // this somehow sets the "allow table to be split across pages", and not the "allow row to be split across pages"
        StyleMayBreakBetweenRowsAttribute attr =
            new StyleMayBreakBetweenRowsAttribute(textDocument.getFileDom(OdfSchemaDocument.OdfXMLFile.CONTENT));
        attr.setBooleanValue(true);
        odfStyle.setAttributeNode(attr);

        for (Row r : tbl.getRowList()) {
            FoKeepTogetherAttribute attr2 = new FoKeepTogetherAttribute(textDocument.getFileDom(OdfSchemaDocument.OdfXMLFile.CONTENT));
            attr2.setEnumValue(FoKeepTogetherAttribute.Value.ALWAYS);
            r.getOdfElement().setOdfAttribute(attr2);
        }
        
        Row r = tbl.appendRow();
        FoKeepTogetherAttribute attr2 = new FoKeepTogetherAttribute(textDocument.getFileDom(OdfSchemaDocument.OdfXMLFile.CONTENT));
        attr2.setEnumValue(FoKeepTogetherAttribute.Value.ALWAYS);
        r.getOdfElement().setOdfAttribute(attr2);

        File targetFile = new File("c:\\temp\\lala.odt");
        textDocument.save(targetFile);
    }
    
    
}
