package tk.djcrazy.MyCC98.request;

import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import tk.djcrazy.libCC98.CachedCC98Service;
import tk.djcrazy.libCC98.data.UserStatueEntity;

/**
 * Created by DJ on 13-7-19.
 */
public class GetFriendsRequest extends SpiceRequest<List> {

    private CachedCC98Service mCC98Service;
    public GetFriendsRequest(CachedCC98Service service) {
        super(List.class );
        mCC98Service = service;
    }
    @Override
    public List<UserStatueEntity> loadDataFromNetwork() throws Exception {

        return null;
    }
}
