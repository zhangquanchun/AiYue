package com.fju.zqc.fjuzqcgradutation.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cy.imagelib.ImageLoaderUtils;
import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.AdapterBase;
import com.cy.widgetlibrary.view.RoundCImageView;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleConnEntity;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleConnNext;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleNextSupport;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;

import java.util.List;
import java.util.logging.Handler;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhang on 2015/10/18.
 */
public class ArticleConnNextAdapter extends AdapterBase<ArticleConnNext> {
    private boolean delayToDo=true;
    public ArticleConnNextAdapter(Context context, List<ArticleConnNext> list) {
        super(context, list);
    }

    @Override
    protected View getItemView(int position, View convertView, ViewGroup parent, ArticleConnNext entity1) {

        if(convertView==null){
           convertView=mInflater.inflate(R.layout.item_conn_next,null);
        }
        final View fView=convertView;
        final ArticleConnNext entity=mList.get(position);
        if(entity.getUser().getFid()==null){
            ((RoundCImageView) getHolderView(convertView, R.id.ivHeader)).setImageResource(R.drawable.ic_launcher);
        }else{
            ImageLoaderUtils.getInstance().loadImage(entity.getUser().getFid()
                    , ((RoundCImageView) getHolderView(convertView, R.id.ivHeader)));
        }
        ((TextView) getHolderView(convertView,R.id.tvTime)).setText(entity.getArticleTime());
        ((TextView)getHolderView(convertView,R.id.articleContent)).setText(entity.getArticleContent());
        ((TextView)getHolderView(convertView,R.id.tvAuthor)).setText(entity.getUser().getUsername());
        ((TextView)getHolderView(convertView,R.id.tvSupportNum)).setText(entity.getSupportNum()+"");
        ((ImageView)getHolderView(convertView,R.id.ivSupport)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!DataStorageUtils.getIsLogIn()){
                    WidgetUtils.showToast("登录用户才可以点赞!");
                    return;
                }
                android.os.Handler handler=new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        delayToDo=true;
                    }
                },6000);
                if (delayToDo){
                    delayToDo=false;
                    BmobQuery<ArticleNextSupport> query=new BmobQuery<ArticleNextSupport>();
                    query.addWhereEqualTo("userName", DataStorageUtils.getUserNickName());
                    query.addWhereEqualTo("articleConnId",entity.getObjectId());
                    query.findObjects(mContext,new FindListener<ArticleNextSupport>() {
                        @Override
                        public void onSuccess(List<ArticleNextSupport> articleNextSupports) {
                            if(articleNextSupports.size()==0){
                                ArticleNextSupport articleNextSupport=new ArticleNextSupport();
                                articleNextSupport.setUserName(DataStorageUtils.getUserNickName());
                                articleNextSupport.setArticleConnId(entity.getObjectId());
                                articleNextSupport.save(mContext,new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        ArticleConnNext articleConnNext=new ArticleConnNext();
                                        articleConnNext.increment("supportNum");
                                        articleConnNext.update(mContext, entity.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void onSuccess() {
                                                ((ImageView)getHolderView(fView,R.id.ivSupport)).setPressed(true);
                                                ((TextView)getHolderView(fView,R.id.tvSupportNum)).setText((entity.getSupportNum()+1)+"");
                                            }
                                            @Override
                                            public void onFailure(int i, String s) {
                                                Toast.makeText(mContext,"操作失败!"+s,Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(mContext,"操作失败!"+s,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                               ((ImageView)getHolderView(fView,R.id.ivSupport)).setPressed(true);
                                WidgetUtils.showToast("您已经点赞了!");
                            }
                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }else{
                    WidgetUtils.showToast("操作过于频繁!");
                }

            }
        });
        return convertView;
    }
}
