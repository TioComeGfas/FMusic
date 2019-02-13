package frodo.fmusic.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import frodo.fmusic.Activities.MainActivity;
import frodo.fmusic.Code.Manager;
import frodo.fmusic.Interfaces.OnItemClicked;
import frodo.fmusic.Modelos.Album;
import frodo.fmusic.Modelos.Song;
import frodo.fmusic.R;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private OnItemClicked onClick;
    private Manager manager;
    private Album album;

    public AlbumAdapter() {
        manager = MainActivity.getManager();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,final int position) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return null;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_titulo) TextView title;
        @BindView(R.id.text_view_artista) TextView artist;
        @BindView(R.id.image_view_caratula_lista) ImageView caratula;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Song song, final OnItemClicked listener){

            title.setText(song.getName());
            artist.setText(song.getArtist().getName());
            Glide.with(itemView)
                    .load(song.getAlbum().getUriAlbumArt())
                    .apply(new RequestOptions()
                            .error(R.drawable.ic_image_caratula_null)
                            .centerCrop())
                    .into(caratula);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

}
