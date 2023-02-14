package edu.qc.seclass.glm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * RECYCLER ADAPTER TO VIEW LISTS
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    // RECYCLER VIEWER VARIABLES
    private Context context;
    private ArrayList<ListModel> arrLists;
    private ArrayList<ItemModel> list;
    private String currUser;
    private ListModel currList;

    // CONSTRUCTOR
    public RecyclerAdapter(Context c, ArrayList<ListModel> a, String u){
        context = c;
        arrLists = a;
        currUser = u;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.one_list_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListModel lm = (ListModel) arrLists.get(position);
        holder.listName.setText(lm.listName);

        // DELETE, RENAME, AND VIEW LISTS BY LONG CLICK
        holder.linearLayoutRow.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                // DIALOG VARIABLES
                currList = lm;
                Dialog dialogMenu = new Dialog(context);
                dialogMenu.setContentView(R.layout.list_management_menu);
                Button btn_delete_list = dialogMenu.findViewById(R.id.btn_delete_list);
                Button btn_rename_the_list = dialogMenu.findViewById(R.id.btn_rename_the_list);
                Button btn_view_list = dialogMenu.findViewById(R.id.btn_view_items);

                // SET DELETE LIST ON CLICK LISTENER
                btn_delete_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // DELETE LIST VARIABLES
                        dialogMenu.dismiss();
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.list_management_delete_list); // goto delete
                        Button btn_delete_the_list = dialog.findViewById(R.id.btn_delete_the_list);
                        Button btn_cancel_to_delete = dialog.findViewById(R.id.btn_cancel_to_delete);

                        // SET CANCEL DELETE LIST ON CLICK LISTENER
                        btn_cancel_to_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        // SET CONFIRM DELETE LIST ON CLICK LISTENER
                        btn_delete_the_list.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                arrLists.remove(position);
                                notifyItemRemoved(position);
                                dialog.dismiss();
                                ListDatabase ld = new ListDatabase(context);
                                ld.updateLists(currUser,arrLists);
                            }
                        });

                        dialog.show();
                    }
                });

                // SET RENAME LIST ON CLICK LISTENER
                btn_rename_the_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // RENAME LIST VARIABLES
                        dialogMenu.dismiss();
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.activity_list_rename_list); // goto  rename
                        EditText et_new_list_name = dialog.findViewById(R.id.et_new_list_name);
                        Button btn_rename_the_list = dialog.findViewById(R.id.btn_rename_the_list);
                        Button btn_cancel_to_rename = dialog.findViewById(R.id.btn_cancel_to_rename);

                        // SET CANCEL RENAME LIST ON CLICK LISTENER
                        btn_cancel_to_rename.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        // SET CONFIRM RENAME LIST ON CLICK LISTENER
                        btn_rename_the_list.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String name = "";

                                if (!et_new_list_name.getText().toString().equals("")){
                                    if (!listExists(et_new_list_name.getText().toString())) {
                                        name = et_new_list_name.getText().toString();
                                        arrLists.set(position,new ListModel(name,currList.items));
                                        notifyItemChanged(position);
                                        dialog.dismiss();
                                        ListDatabase ld = new ListDatabase(context);
                                        ld.updateLists(currUser,arrLists);
                                    }
                                    else {
                                        Toast.makeText(context,"That list already exists.",Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(context,"Empty, please enter a name", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                        dialog.show();
                    }
                });

                // VIEW LIST ON CLICK LISTENER
                btn_view_list.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // VIEW LIST VARIABLES
                        dialogMenu.dismiss();
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.activity_list_view);
                        TextView listName = dialog.findViewById(R.id.listName);
                        listName.setText(currList.listName);
                        TextView itemView = dialog.findViewById(R.id.itemList);
                        Button clearCheckOffs = dialog.findViewById(R.id.clearChecks);

                        // DISPLAY ITEMS IN LIST
                        writeItems(itemView);

                        // SET CLEAR CHECK OFF ON CLICK LISTENER
                        clearCheckOffs.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                for (int i = 0; i < currList.items.size(); i++) {
                                    currList.items.get(i).checkedOff = false;
                                    arrLists.set(position,currList);
                                    ListDatabase ld = new ListDatabase(context);
                                    ld.updateLists(currUser,arrLists);
                                }
                                itemView.setText("");
                                writeItems(itemView);
                                Toast.makeText(context,"All check offs cleared",Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.show();
                    }
                });
                dialogMenu.show();
                return true;
            }
        });
    }

    // DISPLAY ITEMS IN LIST
    public void writeItems(TextView itemView) {
        for (int i = 0; i < currList.items.size(); i++) {
            itemView.append("\nItem Name: " + currList.items.get(i).name +
                    "\nItem Type: " + currList.items.get(i).type +
                    "\nQuantity: " + currList.items.get(i).quantity +
                    "\nChecked Off: " + currList.items.get(i).checkedOff + "\n");
        }
    }

    // DETERMINE WHETHER LIST EXISTS OR NOT
    public boolean listExists(String listName) {
        for (int i = 0; i < arrLists.size(); i++) {
            if (arrLists.get(i).listName.equals(listName)) {
                return true;
            }
        }
        return false;
    }

    // GET NUMBER OF LISTS IN ACCOUNT
    @Override
    public int getItemCount() {
        return (arrLists == null ? 0 : arrLists.size());
    }

    // VIEWHOLDER CLASS: HOLDS EACH LIST
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView listName;
        LinearLayout linearLayoutRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.tv_list_name);
            linearLayoutRow = itemView.findViewById(R.id.linear_layout_row);
        }
    }

}
