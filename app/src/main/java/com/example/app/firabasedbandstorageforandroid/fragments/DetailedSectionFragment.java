package com.example.app.firabasedbandstorageforandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.app.firabasedbandstorageforandroid.AppInterface;
import com.example.app.firabasedbandstorageforandroid.model.Image;
import com.example.app.firabasedbandstorageforandroid.R;
import com.example.app.firabasedbandstorageforandroid.adapters.ImagesRecycleViewAdapter;
import com.example.app.firabasedbandstorageforandroid.manager.AlertManager;
import com.example.app.firabasedbandstorageforandroid.manager.ImageSaveManager;
import com.example.app.firabasedbandstorageforandroid.manager.ToolsManager;
import com.example.app.firabasedbandstorageforandroid.repository.ImageRepositoryImpl;
import com.example.app.firabasedbandstorageforandroid.repository.SectionRepositoryImpl;
import com.example.app.firabasedbandstorageforandroid.repository.interfaces.ImageRepository;
import com.example.app.firabasedbandstorageforandroid.repository.interfaces.SectionRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Tolik on 31.12.2016.
 */

public class DetailedSectionFragment extends BaseFragment {
    private static final int PICK_IMAGE_GALLERY_REQUEST = 2;
    private static final int TAKE_IMAGE_CAMERA_REQUEST = 1;

    @BindView(R.id.recycle_images)
    RecyclerView recycle_images;

    private AppInterface mainActivity;
    private Unbinder unbinder;
    private SectionRepository sectionRepository;
    private ImageRepository imageRepository;
    private ImagesRecycleViewAdapter adapter;
    private String sectionID;
    private String title;
    private String imageName;
    private PopupWindow popupWindow;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (AppInterface) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.section_detail_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);
        this.sectionRepository = new SectionRepositoryImpl(mainActivity.getFireBaseDB());
        this.imageRepository = new ImageRepositoryImpl(mainActivity.getFireBaseStorage());

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recycle_images.setLayoutManager(staggeredGridLayoutManager);

        adapter = new ImagesRecycleViewAdapter((Context) mainActivity);

        adapter.setOnItemClickListener(new ImagesRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onImageDeleteButtonClicked(Image image, int position) {
                deleteImageById(image, position);
            }
        });
        recycle_images.setAdapter(adapter);
        return view;
    }

    void prepareFragment(){
        Bundle args = getArguments();
        sectionID = args.getString("id");
        title = args.getString("name");
        int countOfImages = Integer.parseInt(args.getString("count"));

        String descr = args.getString("descr");
        String lastUpdate = args.getString("last_update");
        String dateOfCreation = args.getString("create_date");

        adapter.setCountImages(countOfImages);
        adapter.setLastUpdate(lastUpdate);
        adapter.setCreationDate(dateOfCreation);
        adapter.setDescription(descr);
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareFragment();

        mainActivity.setToolBarTitle(title);
        mainActivity.setToolBarLeftButtonStyle(false);
        mainActivity.setToolBarLeftButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });
        mainActivity.setToolBarRightButtonStyle(R.style.root_wrap_MainStyle);
        mainActivity.setToolBarRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showPopup();
            }
        });

        if(!ToolsManager.getResumeFlag((Context) mainActivity)){
            getAllImagesBySectionID(sectionID);
        }
    }

    private void showPopup(){
        LayoutInflater layoutInflater = (LayoutInflater) ((Context)mainActivity)
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View popupView = layoutInflater.inflate(R.layout.popup, null);

        popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.showAtLocation(recycle_images, Gravity.TOP|Gravity.RIGHT, 0, 200);

        TextView choosePhoto = (TextView) popupView.findViewById(R.id.choose_photo);
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
                popupWindow.dismiss();
            }
        });

        TextView makePhoto = (TextView) popupView.findViewById(R.id.make_photo);
        makePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoto();
                popupWindow.dismiss();
            }
        });
    }

    private EditText getEditText(){
        EditText editText = new EditText((Context) mainActivity);
        editText.setSingleLine();
        editText.setMaxLines(1);
        return editText;
    }

    public void choosePhoto() {
        final EditText editText = getEditText();
        AlertManager.showInputImageNameDialog((Context) mainActivity, editText, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                imageName = editText.getText().toString();
                if(!imageName.trim().isEmpty()){
                    Intent mIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    mIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(mIntent, getResString(R.string.select_photo)), PICK_IMAGE_GALLERY_REQUEST);
                }else {
                    AlertManager.showToast((Context) mainActivity, getResString(R.string.not_saved));
                }
            }
        }).show();
    }

    public void makePhoto() {
        final EditText editText = getEditText();
        AlertManager.showInputImageNameDialog((Context) mainActivity, editText, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                imageName = editText.getText().toString();
                if(!imageName.trim().isEmpty()){
                    Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(mIntent, TAKE_IMAGE_CAMERA_REQUEST);
                }else {
                    AlertManager.showToast((Context) mainActivity, getResString(R.string.not_saved));
                }
            }
        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            ToolsManager.setResumeFlag(true, (Context) mainActivity);
            if (requestCode == PICK_IMAGE_GALLERY_REQUEST) {
                Uri uri = data.getData();
                ImageSaveManager.saveImageFromURI((Context) mainActivity, uri, imageName);
            }
            if (requestCode == TAKE_IMAGE_CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    ImageSaveManager.writeImage(photo, imageName);
                }
            }
            saveImage(imageName);
        }
    }

    private void getAllImagesBySectionID(final String sectionID) {
        if (ToolsManager.checkWifiOrMobileInternet((Context) mainActivity)) {
            final SweetAlertDialog progressDialog = AlertManager.showSweetProgressBar((Context) mainActivity, getResString(R.string.loading));
            progressDialog.show();

            sectionRepository.getAllImagesInSection(sectionID, new SectionRepository.ImageCallBack() {
                @Override
                public void onSuccess(final List<Image> images) {
                    if(images.isEmpty()){
                        progressDialog.dismiss();
                        return;
                    }

                    final int[] count = {1};
                    final List<Image> results = new ArrayList<Image>();
                    for(final Image i: images){
                        imageRepository.getImageInSection(sectionID, i.getName(), (Context) mainActivity, new ImageRepository.ImageStorageCallBack() {
                            @Override
                            public void onSuccess(Bitmap bitmap) {
                                i.setImageBitmap(bitmap);
                                results.add(i);

                                if(count[0] == images.size()) {
                                    progressDialog.dismiss();
                                    int count = results.size();
                                    adapter.setCountImages(count);
                                    adapter.setImagesCollection(results);
                                }
                                count[0]++;
                            }

                            @Override
                            public void onError() {
                                showError(progressDialog);
                            }
                        });
                    }
                }

                @Override
                public void onError() {
                    showError(progressDialog);
                }
            });

        } else {
            showProblemWithInternet();
        }
    }

    private void deleteImageById(final Image image, final int position) {
        if (ToolsManager.checkWifiOrMobileInternet((Context) mainActivity)) {

            final SweetAlertDialog progressDialog = AlertManager.showSweetProgressBar((Context) mainActivity, getResString(R.string.deleting));
            progressDialog.show();

            final SweetAlertDialog successDialog = AlertManager.showSuccessResult((Context) mainActivity, getResString(R.string.deleted_image))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    });

            sectionRepository.getCountOfImages(sectionID, new SectionRepository.GetResultCallBack() {
                @Override
                public void onSuccess(String result) {
                    final Integer count = Integer.parseInt(result) - 1;

                    imageRepository.deleteImageFromStorage(sectionID, image.getName(), new ImageRepository.SimpleCallBack() {
                        @Override
                        public void onSuccess() {
                            sectionRepository.deleteImageFromSection(sectionID,count, ToolsManager.getCurrentDate(), image.getId(), new SectionRepository.SimpleCallBack() {
                                @Override
                                public void onSuccess() {
                                    adapter.setCountImages(count);
                                    adapter.setLastUpdate(ToolsManager.getCurrentDate());
                                    adapter.removeItem(position);

                                    progressDialog.dismiss();
                                    successDialog.show();
                                }

                                @Override
                                public void onError() {
                                    showError(progressDialog);
                                }
                            });
                        }

                        @Override
                        public void onError() {
                            showError(progressDialog);
                        }
                    });
                }

                @Override
                public void onError() {
                    showError(progressDialog);
                }
            });

        } else {
            showProblemWithInternet();
        }
    }

    public void saveImage(final String imageName) {

        if (ToolsManager.checkWifiOrMobileInternet((Context) mainActivity)) {

            final SweetAlertDialog progressDialog = AlertManager.showSweetProgressBar((Context) mainActivity, getResString(R.string.saving));
            progressDialog.show();

            final SweetAlertDialog successDialog = AlertManager.showSuccessResult((Context) mainActivity, getResString(R.string.saved_image))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            ImageSaveManager.deleteImageByName(imageName);
                        }
                    });

            sectionRepository.getCountOfImages(sectionID, new SectionRepository.GetResultCallBack() {
                @Override
                public void onSuccess(String result) {
                    final Integer count = Integer.parseInt(result) + 1;

                    imageRepository.saveImageInStorage(sectionID, imageName, new ImageRepository.SimpleCallBack() {
                        @Override
                        public void onSuccess() {
                            sectionRepository.saveImageInSection(sectionID, count, ToolsManager.getCurrentDate(), imageName, new SectionRepository.GetResultCallBack() {
                                @Override
                                public void onSuccess(final String imageID) {
                                    Image image = new Image(imageID, imageName, ToolsManager.getCurrentDate());
                                    image.setImageBitmap(ImageSaveManager.readImage(imageName));

                                    List<Image> images  = new ArrayList<Image>(adapter.getImagesCollection());
                                    images.add(image);
                                    adapter.setCountImages(count);
                                    adapter.setLastUpdate(ToolsManager.getCurrentDate());
                                    adapter.setImagesCollection(images);

                                    progressDialog.dismiss();
                                    successDialog.show();
                                }

                                @Override
                                public void onError() {
                                    showError(progressDialog);
                                }
                            });
                        }

                        @Override
                        public void onError() {
                            showError(progressDialog);
                        }
                    });
                }

                @Override
                public void onError() {
                    showError(progressDialog);
                }
            });

        } else {
            showProblemWithInternet();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder = null;
        }
        if(popupWindow != null){
            popupWindow.dismiss();
        }
        ToolsManager.setResumeFlag(false, (Context) mainActivity);
    }
}
