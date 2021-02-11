package pl.edu.uwr.pum.air_app.menu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * for sliding menu only - Created by yarolegovich on 25.03.2017.
 */

@SuppressWarnings({"rawtypes", "ConstantConditions"})
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private List<DrawerItem> items;
    private Map<Class<? extends DrawerItem>, Integer> viewTypes;
    private SparseArray<DrawerItem> holderFactories;
    private static Context mContext;
    private OnItemSelectedListener listener;
    private String url;
    public DrawerAdapter(List<DrawerItem> items, Context context) {
        this.items = items;
        this.viewTypes = new HashMap<>();
        this.holderFactories = new SparseArray<>();
        this.mContext= context;
        processViewTypes();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = holderFactories.get(viewType).createViewHolder(parent);
        holder.adapter = this;
        return holder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        items.get(position).bindViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypes.get(items.get(position).getClass());
    }

    private void processViewTypes() {
        int type = 0;
        for (DrawerItem item : items) {
            if (!viewTypes.containsKey(item.getClass())) {
                viewTypes.put(item.getClass(), type);
                holderFactories.put(type, item);
                type++;
            }
        }
    }

    public void setSelected(int position) {
        DrawerItem newChecked = items.get(position);
        if (!newChecked.isSelectable()) {
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            DrawerItem item = items.get(i);
            if (item.isChecked()) {
                item.setChecked(false);
                notifyItemChanged(i);
                break;
            }
        }

        newChecked.setChecked(true);
        notifyItemChanged(position);

        if (listener != null) {
            listener.onItemSelected(position);
        }
    }

    public void setListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    static abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private DrawerAdapter adapter;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            adapter.setSelected(getAdapterPosition());
            System.out.println(getAdapterPosition());
            switch (getAdapterPosition()){
                case 1:
                    String url = "https://powietrze.gios.gov.pl/pjp/content/annual_assessment_air_acceptable_level";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                    break;
                case 2:
                    String url1 = "https://www.airqlab.pl/polair_ZW.php";
                    Intent i1 = new Intent(Intent.ACTION_VIEW);
                    i1.setData(Uri.parse(url1));
                    mContext.startActivity(i1);
                    break;
                case 3:
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "mikolaj.bozecki8@gmail.com"));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Temat");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Tekst");
                    mContext.startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
            }
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
