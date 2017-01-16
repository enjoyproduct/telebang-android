package com.inspius.yo365.modules.upload_video;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.inspius.coreapp.helper.InspiusIntentUtils;
import com.inspius.yo365.R;
import com.inspius.yo365.base.BaseAppSlideFragment;
import com.inspius.yo365.fragment.VideoSearchFragment;
import com.inspius.yo365.helper.AppUtil;
import com.inspius.yo365.helper.DialogUtil;
import com.inspius.yo365.model.CategoryJSON;
import com.inspius.yo365.model.VideoJSON;
import com.inspius.yo365.model.VideoModel;
import com.inspius.yo365.service.AppSession;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MUploadVideoFragment extends BaseAppSlideFragment {
    public static final String TAG = MUploadVideoFragment.class.getSimpleName();

    public static MUploadVideoFragment newInstance() {
        MUploadVideoFragment fragment = new MUploadVideoFragment();
        return fragment;
    }

    @BindView(R.id.tvnHeaderTitle)
    TextView tvnHeaderTitle;

    @BindView(R.id.tvnFileName)
    TextView tvnFileName;

    @BindView(R.id.editDescription)
    EditText editDescription;

    @BindView(R.id.edtTitle)
    EditText edtTitle;

    @BindView(R.id.tvnCategory)
    TextView tvnCategories;

    @BindView(R.id.btnChooseVideo)
    Button btnChooseVideo;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.imvVideoThumbnail)
    ImageView imvVideoThumbnail;

    private MThumbnailFile thumbnailFile;
    private MVideoFile videoFile;
    private List<Integer> mSelectedItems;
    private List<CategoryJSON> listCategory;

    @Override
    public int getLayout() {
        return R.layout.m_fragment_upload_video;
    }

    @Override
    public String getTagText() {
        return TAG;
    }

    @Override
    public void onInitView() {
        mSelectedItems = new ArrayList<>();  // Where we track the selected items
        updateHeaderTitle(getString(R.string.menu_upload));

        listCategory = AppSession.getInstance().getCategoryData().listCategory;
    }

    @OnClick(R.id.imvHeaderMenu)
    void doShowMenu() {
        mAppActivity.toggleDrawer();
    }

    void updateHeaderTitle(String name) {
        if (TextUtils.isEmpty(name))
            name = getString(R.string.app_name);

        tvnHeaderTitle.setText(name);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MUploadConstant.REQUEST_SELECT_VIDEO) {
                Uri uri = data.getData();

                File file = new File(MUploadHelper.getPath(mContext, uri));
                if (file != null) {
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                            MimeTypeMap.getFileExtensionFromUrl((Uri.fromFile(file)
                                    .toString())));
                    videoFile = new MVideoFile(file, mimeType);

                    Glide.with(mContext).load(uri).listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            thumbnailFile = null;
                            imvVideoThumbnail.setImageResource(R.drawable.no_image_default);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            String mimeType = "image/jpeg";
                            String name = "thumbnail";
                            Bitmap bitmap = MUploadHelper.drawableToBitmap(resource);
                            thumbnailFile = new MThumbnailFile(name, mimeType, bitmap);
                            return false;
                        }
                    }).placeholder(R.drawable.no_image_default).into(imvVideoThumbnail);
                }

                /**
                 * try to do something there
                 * selectedVideoPath is path to the selected video
                 */
                String fileName = MUploadHelper.getFileNameByUri(mContext, uri);
                tvnFileName.setText(fileName);

            } else if (requestCode == MUploadConstant.REQUEST_SELECT_PHOTO) {
                try {
                    Uri uri = data.getData();
                    Glide.with(mContext).load(uri).listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            thumbnailFile = null;
                            imvVideoThumbnail.setImageResource(R.drawable.no_image_default);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            String mimeType = "image/jpeg";
                            String name = "thumbnail";
                            Bitmap bitmap = MUploadHelper.drawableToBitmap(resource);
                            thumbnailFile = new MThumbnailFile(name, mimeType, bitmap);
                            return false;
                        }
                    }).placeholder(R.drawable.no_image_default).into(imvVideoThumbnail);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick(R.id.imvHeaderSearch)
    void doShowSearch() {
        mHostActivity.addFragment(VideoSearchFragment.newInstance());
    }

    @OnClick(R.id.btnSubmit)
    void doSubmit() {
        if (!mCustomerManager.isLogin()) {
            DialogUtil.showMessageBox(mContext, getString(R.string.msg_request_login));
            return;
        }

        String title = edtTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            DialogUtil.showMessageBox(mContext, "Please enter title video");

            return;
        }

        if (videoFile == null) {
            DialogUtil.showMessageBox(mContext, "Please choose a video");
            return;
        }

        stateUploading();

        final ProgressDialog dialogLoading = DialogUtil.showLoading(mContext, "Processing...");
        MVideoUploadModel mVideo = new MVideoUploadModel(title, thumbnailFile, videoFile);

        mVideo.setVideoDes(editDescription.getText().toString());

        if (mSelectedItems.size() > 0) {
            int catIds[] = new int[mSelectedItems.size()];
            int i = 0;
            for (int position : mSelectedItems) {
                catIds[i] = listCategory.get(position).id;
                i++;
            }

            String strCatIds = Arrays.toString(catIds).replaceAll("[\\[\\]\\ ]", "");
            mVideo.setCategoryId(strCatIds);
        }

        MUploadRPC.requestUpLoadVideo(mCustomerManager.getAccountID(), mVideo, new MUploadListener() {
            @Override
            public void onError(String message) {
                dialogLoading.dismiss();
                setEnabled(true);
                DialogUtil.showMessageBox(mContext, message);
            }

            @Override
            public void onSuccess(Object results) {
                dialogLoading.dismiss();
                stateAddInfo();
                final VideoJSON videoResponse = (VideoJSON) results;
                Toast.makeText(mContext, "Upload successfully", Toast.LENGTH_SHORT).show();
                startActivity(AppUtil.getIntentVideoDetail(mContext, new VideoModel(videoResponse), false));
            }

            @Override
            public void onProgress(int percent) {
                dialogLoading.setMessage(Html.fromHtml(String.format("Processing... %s&#37;", percent)));
            }
        });
    }

    void stateUploading() {
        btnSubmit.setText("Video is currently uploading");
        setEnabled(false);
    }

    void stateAddInfo() {
        btnSubmit.setText(getString(R.string.cm_submit));

        setEnabled(true);

        edtTitle.setText("");
        editDescription.setText("");
        tvnCategories.setText("Choose video categories");
        mSelectedItems.clear();
        tvnFileName.setText("No file");
        btnChooseVideo.setText(getString(R.string.upload_choose_video));
        imvVideoThumbnail.setImageResource(R.drawable.no_image_default);

        thumbnailFile = null;
        videoFile = null;
    }

    void setEnabled(boolean enabled) {
        btnSubmit.setEnabled(enabled);
        edtTitle.setEnabled(enabled);
        btnChooseVideo.setEnabled(enabled);
    }

    @OnClick(R.id.btnChooseVideo)
    void doChooseVideo() {
        if (!AppUtil.verifyStoragePermissions(getActivity()))
            return;

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, MUploadConstant.REQUEST_SELECT_VIDEO);
    }

    @OnClick(R.id.imvVideoThumbnail)
    void doChooseThumbnail() {
        if (!AppUtil.verifyStoragePermissions(getActivity()))
            return;

        Intent i = InspiusIntentUtils.pickImage();
        startActivityForResult(i, MUploadConstant.REQUEST_SELECT_PHOTO);
    }

    @OnClick(R.id.tvnCategory)
    void doChooseCategory() {
        Dialog dialog = createDialogChooseCategories();
        dialog.show();
    }

    public Dialog createDialogChooseCategories() {
        final String[] categories = new String[listCategory.size()];
        final boolean[] checkedCategories = new boolean[listCategory.size()];

        for (int i = 0; i < categories.length; i++) {
            categories[i] = listCategory.get(i).name;
        }

        for (int position : mSelectedItems) {
            checkedCategories[position] = true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.upload_select_categories)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(categories, checkedCategories,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                checkedCategories[which] = isChecked;
                            }
                        })

                // Set the action buttons
                .setPositiveButton(R.string.cm_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mSelectedItems.clear();
                        String strCat = "";
                        for (int i = 0; i < categories.length; i++)
                            if (checkedCategories[i]) {
                                mSelectedItems.add(i);
                                strCat += categories[i] + ", ";
                            }

                        tvnCategories.setText(strCat);
                    }
                })
                .setNegativeButton(R.string.cm_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
}
