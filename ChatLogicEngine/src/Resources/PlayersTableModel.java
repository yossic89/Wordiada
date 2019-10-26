package Resources;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by yossi_c89 on 26/05/2017.
 */
public class PlayersTableModel {
        public SimpleIntegerProperty id = new SimpleIntegerProperty();
        public SimpleStringProperty name = new SimpleStringProperty();


        public PlayersTableModel(int id)
        {
            this.id.set(id);
        }
        public Integer getId() {
            return id.get();
        }


}
