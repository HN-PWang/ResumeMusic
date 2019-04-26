package com.leancloud.home.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leancloud.home.R;
import com.vise.xsnow.event.BusManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.com.commoncore.avdb.AVDbGlobal;
import tech.com.commoncore.event.SwitchEvent;
import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.DateUtil;
import tech.com.commoncore.utils.ToastUtil;

import static tech.com.commoncore.avdb.AVDbManager.ATTENTION_ISDELETE;
import static tech.com.commoncore.avdb.AVDbManager.ATTENTION_OBSERVED_USER;
import static tech.com.commoncore.avdb.AVDbManager.ATTENTION_USER;
import static tech.com.commoncore.avdb.AVDbManager.COLLECT_TYPE;
import static tech.com.commoncore.avdb.AVDbManager.COLLECT_TYPE_COMMUNITY;
import static tech.com.commoncore.avdb.AVDbManager.COLLECT_USER;
import static tech.com.commoncore.avdb.AVDbManager.COMMUNITY_COLLECTION_COUNT;
import static tech.com.commoncore.avdb.AVDbManager.COMMUNITY_COMMENT_COUNT;
import static tech.com.commoncore.avdb.AVDbManager.COMMUNITY_CONTENT;
import static tech.com.commoncore.avdb.AVDbManager.COMMUNITY_IMG_LIST;
import static tech.com.commoncore.avdb.AVDbManager.COMMUNITY_LIKE_COUNT;
import static tech.com.commoncore.avdb.AVDbManager.COMMUNITY_NAME;
import static tech.com.commoncore.avdb.AVDbManager.COMMUNITY_USER;
import static tech.com.commoncore.avdb.AVDbManager.COMMUNITY_USER_HEAD;
import static tech.com.commoncore.avdb.AVDbManager.LIKE_COMMUNITY;
import static tech.com.commoncore.avdb.AVDbManager.LIKE_USER;
import static tech.com.commoncore.avdb.AVDbManager.TABLE_ATTENTION;
import static tech.com.commoncore.avdb.AVDbManager.TABLE_COLLECT;
import static tech.com.commoncore.avdb.AVDbManager.TABLE_LIKE;
import static tech.com.commoncore.utils.DateUtil.FORMAT_5;

public class HomeCircleAdapter extends BaseQuickAdapter<AVObject, BaseViewHolder> {
    private List<AVObject> myAttentionList = new ArrayList<>();    //关注对象集合
    private List<AVObject> myLikeList = new ArrayList<>();         //点赞对象集合
    private List<AVObject> myCollectionList = new ArrayList<>();   //收藏对象集合

    public HomeCircleAdapter(int layoutResId) {
        super(layoutResId);
    }

    public void setAttentionList(List<AVObject> attentions) {
        this.myAttentionList.clear();
        this.myAttentionList.addAll(attentions);
    }

    public void setMyLikeList(List<AVObject> myLikeList) {
        this.myLikeList.clear();
        this.myLikeList.addAll(myLikeList);
    }

    public void setMyCollectionList(List<AVObject> myCollectionList) {
        this.myCollectionList.clear();
        this.myCollectionList.addAll(myCollectionList);
    }

    @Override
    protected void convert(BaseViewHolder helper, final AVObject item) {
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusManager.getBus().post(new SwitchEvent(2));
            }
        });

        String headUrl = String.valueOf(item.get(COMMUNITY_USER_HEAD));
        String nike = String.valueOf(item.get(COMMUNITY_NAME));
        Date date = item.getCreatedAt();
        String dateStr = DateUtil.formatDate(date, FORMAT_5);
        String userId = String.valueOf(item.get(COMMUNITY_USER));
        String content = String.valueOf(item.get(COMMUNITY_CONTENT));
        String imgStr = String.valueOf(item.get(COMMUNITY_IMG_LIST));
        String[] image = imgStr.split(",");
        String collectionCount = String.valueOf(item.get(COMMUNITY_COLLECTION_COUNT));
        String commetCount = String.valueOf(item.get(COMMUNITY_COMMENT_COUNT));
        String likeCount = String.valueOf(item.get(COMMUNITY_LIKE_COUNT));

        ImageView head = helper.itemView.findViewById(R.id.circle_item_head_image);
        GlideManager.loadRoundImg(headUrl, head);
        ViewPager viewPager = helper.itemView.findViewById(R.id.circle_item_content_image);
        final TextView collectionTx = helper.itemView.findViewById(R.id.circle_item_collection);
        TextView commentTx = helper.itemView.findViewById(R.id.circle_item_comment);
        final TextView likeTx = helper.itemView.findViewById(R.id.circle_item_like);
        final TextView attentionTx = helper.itemView.findViewById(R.id.circle_item_attention_text);
        collectionTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AVUser.getCurrentUser() == null) {
                    ToastUtil.show("您还没有登录哦");
                    return;
                }
                boolean isCollection = (getCollection(item.getObjectId()) != null);
                handlerCollectionClick(collectionTx, isCollection, item);
            }
        });
        commentTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AVUser.getCurrentUser() == null) {
                    ToastUtil.show("您还没有登录哦");
                    return;
                }
            }
        });
        likeTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AVUser.getCurrentUser() == null) {
                    ToastUtil.show("您还没有登录哦");
                    return;
                }
                boolean isLike = (getMyLike(item.getObjectId()) != null);
                handlerLikeClick(likeTx, isLike, item);
            }
        });
        attentionTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AVUser.getCurrentUser() == null) {
                    ToastUtil.show("您还没有登录哦");
                    return;
                }
                boolean isAttention = getAttentionOb((String) item.get(ATTENTION_OBSERVED_USER)) != null;
                handlerAttentionClick(attentionTx, isAttention, item);
            }
        });

        helper.setText(R.id.circle_item_name_text, nike);
        helper.setText(R.id.circle_item_date_text, dateStr);
        helper.setText(R.id.circle_item_content_text, content);
        helper.setText(R.id.circle_item_collection, collectionCount);
        helper.setText(R.id.circle_item_comment, commetCount);
        helper.setText(R.id.circle_item_like, likeCount);

        if (getAttentionOb(userId) != null) {
            handlerAttentionView(attentionTx, true);
        } else {
            handlerAttentionView(attentionTx, false);
        }
        if (getMyLike(item.getObjectId()) != null) {
            handlerLikeView(likeTx, true);
        } else {
            handlerLikeView(likeTx, false);
        }
        if (getCollection(item.getObjectId()) != null) {
            handlerCollectionView(collectionTx, true);
        } else {
            handlerCollectionView(collectionTx, false);
        }
    }

    private void handlerCollectionClick(TextView tx, boolean isCollection, AVObject object) {
        String communityId = object.getObjectId();
        if (!isCollection) {
            int collectionCount = Integer.parseInt(tx.getText().toString());
            tx.setText(collectionCount + 1 + "");
            handlerCollectionView(tx, true);

            AVObject collect = new AVObject(TABLE_COLLECT);
            collect.put(COLLECT_TYPE_COMMUNITY, communityId);
            collect.put(COLLECT_TYPE, COLLECT_TYPE_COMMUNITY);
            collect.put(COLLECT_USER, AVUser.getCurrentUser().getObjectId());
            myCollectionList.add(collect);
            AVDbGlobal.getInstance().getAVDb().addCollect(collect, new SaveCallback() {
                @Override
                public void done(AVException e) {
                    ToastUtil.show("收藏成功");
                }
            });
        } else {
            int collectionCount = Integer.parseInt(tx.getText().toString());
            tx.setText(collectionCount - 1 + "");
            handlerCollectionView(tx, false);

            if (getCollection(communityId) == null) {
                return;
            }
            AVDbGlobal.getInstance().getAVDb().deleteCollect(getCollection(communityId).getObjectId(), communityId, null);
            for (int i = 0; i < myCollectionList.size(); i++) {
                AVObject collect = myCollectionList.get(i);
                String id = (String) collect.get(COLLECT_TYPE_COMMUNITY);
                if (id != null && id.equals(communityId)) {
                    myCollectionList.remove(i);
                }
            }
        }
    }

    private void handlerLikeClick(TextView tx, boolean isLike, AVObject object) {
        if (!isLike) {
            AVObject like = new AVObject(TABLE_LIKE);
            like.put(LIKE_COMMUNITY, object.getObjectId());
            like.put(LIKE_USER, AVUser.getCurrentUser().getObjectId());
//            myLikeList.add(like);
            AVDbGlobal.getInstance().getAVDb().addLike(like, null);
            int likeCount = Integer.parseInt(tx.getText().toString());
            tx.setText(likeCount + 1 + "");
            handlerLikeView(tx, true);
        }
    }

    private void handlerAttentionClick(TextView tx, boolean isAttention, AVObject object) {
        String obUserId = object.get(COMMUNITY_USER).toString();
        if (!isAttention) {
            handlerAttentionView(tx, true);

            AVObject attention = new AVObject(TABLE_ATTENTION);
            attention.put(ATTENTION_USER, AVUser.getCurrentUser().getObjectId());
            attention.put(ATTENTION_OBSERVED_USER, obUserId);
            attention.put(ATTENTION_ISDELETE, 0);
            myAttentionList.add(attention);
            AVDbGlobal.getInstance().getAVDb().addAttention(attention, null);
        } else {
            handlerAttentionView(tx, false);

            AVDbGlobal.getInstance().getAVDb().deleteAttention(getAttentionOb(obUserId).getObjectId(), null);

            for (int i = 0; i < myAttentionList.size(); i++) {
                AVObject attention = myAttentionList.get(i);
                String obUser = attention.get(ATTENTION_OBSERVED_USER).toString();
                if (obUser.equals(obUserId)) {
                    myAttentionList.remove(i);
                }
            }
        }
    }

    private void handlerCollectionView(TextView tx, boolean isCollection) {
        Drawable drawable;
        if (isCollection) {
            drawable = mContext.getResources().getDrawable(R.drawable.icon_collection);
        } else {
            drawable = mContext.getResources().getDrawable(R.drawable.icon_un_collection);
        }
        tx.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    private void handlerLikeView(TextView tx, boolean isLike) {
        Drawable drawable;
        if (isLike) {
            drawable = mContext.getResources().getDrawable(R.drawable.icon_like);
        } else {
            drawable = mContext.getResources().getDrawable(R.drawable.icon_un_like);
        }
        tx.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    private void handlerAttentionView(TextView tx, boolean isAttention) {
        if (isAttention) {
            tx.setText("已关注");
            tx.setBackground(mContext.getResources().getDrawable(R.drawable.home_bg_attention));
        } else {
            tx.setText("关注 +");
            tx.setBackground(mContext.getResources().getDrawable(R.drawable.home_bg_un_attention));
        }
    }

    private AVObject getAttentionOb(String obUserId) {
        if (myAttentionList == null) {
            return null;
        }
        for (AVObject object : myAttentionList) {
            String userId = (String) object.get(ATTENTION_OBSERVED_USER);
            if (userId != null) {
                if (userId.equals(obUserId)) {
                    return object;
                }
            }
        }
        return null;
    }

    private AVObject getMyLike(String communityId) {
        if (myLikeList == null) {
            return null;
        }
        for (AVObject object : myLikeList) {
            String id = (String) object.get(LIKE_COMMUNITY);
            if (id != null) {
                if (id.equals(communityId)) {
                    return object;
                }
            }
        }
        return null;
    }

    private AVObject getCollection(String communityId) {
        if (myCollectionList == null) {
            return null;
        }
        for (AVObject object : myCollectionList) {
            String id = (String) object.get(COLLECT_TYPE_COMMUNITY);
            if (id != null) {
                if (id.equals(communityId)) {
                    return object;
                }
            }
        }
        return null;
    }
}