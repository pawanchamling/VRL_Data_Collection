package no.ntnu.pawanchamling.vrldatacollection.session;

/**
 * Created by Pawan Chamling on 04/04/15.
 */
public class DialogPresenter {

    private NoteDialogView noteDialogView;

    public DialogPresenter(NoteDialogView noteDialogView){
        this.noteDialogView = noteDialogView;
    }


    public void saveNote(String value) {
        noteDialogView.saveNote(value);
    }

}
