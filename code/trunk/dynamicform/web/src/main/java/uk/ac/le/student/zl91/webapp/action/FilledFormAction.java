package uk.ac.le.student.zl91.webapp.action;

import org.apache.struts2.ServletActionContext;
import org.appfuse.model.User;
import org.appfuse.service.GenericManager;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import uk.ac.le.student.zl91.model.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Blob;
import java.util.*;

/**
 * The class handles creating, updating, deleting and preparing data on
 * the pages of filled form.
 *
 * <p/>
 * Created on 2014/4/2.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public class FilledFormAction extends BaseAction {

    private GenericManager<FilledForm, Long> filledFormManager;
    @Autowired
    public void setFilledFormManager(GenericManager<FilledForm, Long> filledFormManager) {
        this.filledFormManager = filledFormManager;
    }

    private GenericManager<Template, Long> templateManager;

    public void setTemplateManager(GenericManager<Template, Long> templateManager) {
        this.templateManager = templateManager;
    }

    private GenericManager<Response, Long> responseManager;
    @Autowired
    public void setResponseManager(GenericManager<Response, Long> responseManager) {
        this.responseManager = responseManager;
    }

    private GenericManager<Answer, Long> answerManager;
    @Autowired
    public void setAnswerManager(GenericManager<Answer, Long> answerManager) {
        this.answerManager = answerManager;
    }

    private GenericManager<Question, Long> questionManager;
    @Autowired
    public void setQuestionManager(GenericManager<Question, Long> questionManager) {
        this.questionManager = questionManager;
    }

    //the list of filled form in the list page.
    private List filledForms;

    //the current object of filled form
    private FilledForm filledForm;
    //the current id of filled form
    private Long ffid;

    public Long getFfid() {
        return ffid;
    }

    public void setFfid(Long ffid) {
        this.ffid = ffid;
    }

    public FilledForm getFilledForm() {
        return filledForm;
    }

    public void setFilledForm(FilledForm filledForm) {
        this.filledForm = filledForm;
    }

    public List getFilledForms() {
        return filledForms;
    }

    //the object of files which are uploaded with a filled form
    private List<File> uploads = new ArrayList<File>();
    private List<String> uploadFileNames = new ArrayList<String>();
    private List<String> uploadContentTypes = new ArrayList<String>();


    public List<File> getUpload() {
        return this.uploads;
    }
    public void setUpload(List<File> uploads) {
        this.uploads = uploads;
    }

    public List<String> getUploadFileName() {
        return this.uploadFileNames;
    }
    public void setUploadFileName(List<String> uploadFileNames) {
        this.uploadFileNames = uploadFileNames;
    }

    public List<String> getUploadContentType() {
        return this.uploadContentTypes;
    }
    public void setUploadContentType(List<String> contentTypes) {
        this.uploadContentTypes = contentTypes;
    }

    //executed when open the list page of filled form.
    public String list() {
        filledForms = filledFormManager.getAll();
        log.debug("filledForms.size:" + filledForms.size());
        return SUCCESS;
    }


    /**
     * executed when delete a filled form
     * @return delete
     */
    public String delete() {
        this.filledForm = this.filledFormManager.get(this.filledForm.getFfid());
        for (Response response : this.filledForm.getResponseSet()){
            this.responseManager.remove(response);
        }

        filledFormManager.remove(filledForm.getFfid());
        saveMessage(getText("filledForm.deleted"));

        return "delete";//for redirect to filled form list
    }

    //the list of questions which build up the content of the current
    // filled form on page for use filling it up
    private List<Question> filledFormView;

    /**
     * the method of build the view of filled form page.
     * @param filledForm: the filled form which is supposed to shown on the
     *                  page for filling up
     * @return: a answer list without duplicated answer. those answers are
     *                  with their responses.
     */
    private List<Question> buildFilledFormView(FilledForm filledForm){

        //pass the content from responses to answers, By
        //setting value of FilledForm->Response.content
        //to FilledForm->Template->Question->Answer.content
        Set<Question> tquestionSet= filledForm.getTemplate().getQuestionSet();
        Set<Response> responseSet = filledForm.getResponseSet();
        for(Iterator<Question> itq = tquestionSet.iterator(); itq.hasNext(); ) {
            Question tq = itq.next();
            Set<Answer> tqanswerSet = tq.getAnswerSet();
            for(Iterator<Answer> ita = tqanswerSet.iterator(); ita.hasNext(); ) {
                Answer tqa = ita.next();
                for(Iterator<Response> itr = responseSet.iterator(); itr.hasNext(); ) {
                    Response r = itr.next();
//                    log.debug("fill content Qid:"
//                            + r.getQuestion().getQid() + ":" + tq.getQid()
//                            + "; Aid:" + r.getAnswer().getAid() + ":" + tqa.getAid());
                    //put the content and rid of a response to the answer
                    if (r.getAnswer().getAid() == tqa.getAid()
                            && r.getQuestion().getQid() == tq.getQid()){
                        tqa.setContent(r.getContent());
                        tqa.setRid(r.getRid());
//                        log.debug("fill content to Qid:" + r.getQuestion().getQid()
//                                + "; Aid:" + tqa.getAid() + "; Content:" + r.getContent());
                    }
                }
            }
        }

        List<Question> filledFormView = this.buildTemplateView(filledForm.getTemplate());
        return filledFormView;
    }

    /**
     * Re-arrange the structure of questions within the template
     * @param template: the template of a filled form
     * @return a answer list without duplicated answer
     */
    private List<Question> buildTemplateView(Template template){
        return removedDuplicatedNestedQuestions(template);
    }

    /**
     * remove those duplicated and nested questions from a template.
     * make sure there is one question shown in the form once.
     * @param template : the template of a filled form
     * @return a answer list without duplicated answer
     */
    private List<Question> removedDuplicatedNestedQuestions(Template template){

        //loop with questions: scanning each question; if there is a sub-question,
        // then remove the question from top level
        Set<Question> removedQuestionSet = new HashSet<Question>();
//        Set<Question> t_questionSet = template.getQuestionSet();
        for(Iterator<Question> it = template.getQuestionSet().iterator(); it.hasNext(); ) {

            Question t_question = it.next();

            removedQuestionSet.addAll(t_question.getQuestionSet());
//            if(t_question.getQuestionSet().size()!=0)
//            log.debug("t_question.getQuestionSet().size():"+t_question.getQuestionSet().size()
//                    +", removedQuestionSet.size():"+removedQuestionSet.size());



            //Re-arrange the structure of Question for presentation
            //the Answers of the current question are re-arranged.
            removeDuplicatedReferredAnswers(t_question);
            removeDuplicatedReferredQuestions(t_question,template.getQuestionSet());
        }
//        t_questionSet.removeAll(removedQuestionSet);
//        log.debug("template.getQuestionSet().size():"+template.getQuestionSet().size()
//                +", removedQuestionSet.size():"+removedQuestionSet.size());

        template.getQuestionSet().removeAll(removedQuestionSet);

//        log.debug("template.getQuestionSet().size():"+template.getQuestionSet().size());



        List<Question> templateView = new ArrayList<Question>(template.getQuestionSet());
        Collections.sort(templateView);
        return templateView;

    }
    /**
     * Re-arrange the structure of Question for presentation
     * the Answers of the current question are re-arranged.
     */
    private void removeDuplicatedReferredAnswers(Question question){
        Set<Answer> answerSet = question.getAnswerSet();
        Set<Answer> removedAnswerSet = new HashSet<Answer>();
//        log.debug("answerSet.size():"+answerSet.size()
//                +", removedAnswerSet.size():"+removedAnswerSet.size());
        for(Iterator<Answer> it = question.getAnswerSet().iterator(); it.hasNext(); ) {
            Answer a = it.next();
            if (a.getReferredAnswer()!=null){
//                log.debug("remove answer [id]:"+a.getReferredAnswer().getAid()
//                +" from question [id]:"+question.getQid());
                removedAnswerSet.add(a.getReferredAnswer());
            }
        }
        answerSet.removeAll(removedAnswerSet);
    }

    //loop with answers: scanning each answer; if there is a referred question,
    // then remove the question from top level
    private void removeDuplicatedReferredQuestions(Question question, Set<Question> t_questionSet){

        Set<Question> removedQuestionSet = new HashSet<Question>();
        for(Iterator<Answer> it = question.getAnswerSet().iterator(); it.hasNext(); ) {
            Answer a = it.next();
            if (a.getReferredQuestion()!=null){
//                log.debug("remove question [id]:"+a.getReferredQuestion().getQid()
//                        +" from answer [id]:"+a.getAid());
                removedQuestionSet.add(a.getReferredQuestion());
            }
        }
        t_questionSet.removeAll(removedQuestionSet);
    }

    // the id of a template, when starting a new filled form form a template,
    // it is assigned.
    private Long tid;

    public void setTid(Long tid) {
        this.tid = tid;
    }

    //executed when a user view the report of a filled form
    public String report() {
        return this.edit();
    }

    //executed when a user click the update button
    public String edit() {
        log.debug("tid:" + tid);//fill a new form from template
        log.debug("ffid:" + ffid);//update an existed filled form
        if (ffid != null) {//update the filled form
            filledForm = filledFormManager.get(ffid);
        } else {//create a new filled form
            filledForm = new FilledForm();
            this.filledForm.setTemplate(this.templateManager.get(tid));
        }
        initDynamicFormParameters();
        return SUCCESS;
    }

    //initialise the values of parameters on the edit page of filled form
    private void initDynamicFormParameters() {

        this.filledFormView = this.buildFilledFormView(this.filledForm);
        this.getRequest().setAttribute("filledFormView",filledFormView);
//        log.debug("filledForm:"+filledForm);
//        log.debug("getTemplate():"+filledForm.getTemplate());
    }

    /**
     * executed when a user click download button for downloading a file
     * within the filled form.
     */
    public void download() {

        long rid = Long.parseLong(this.getRequest().getParameter("rid"));
        Response r = this.responseManager.get(rid);

        HttpServletResponse response = this.getResponse();
        ServletContext context = ServletActionContext.getServletContext();
        // the type of file
        String fileType = (r.getFileType() == null || r.getFileType().length() == 0)
                ? "multipart/form-data"
                : r.getFileType();
        log.debug("downloading fileType:"+fileType);
        response.setContentType(fileType);
        response.setHeader("content-disposition",
                "attachment;filename=" + r.getContent());


        try {
            OutputStream os = response.getOutputStream();
//            byte[] tempBytes = new byte[10240];
//            int byteWritten = 0;
            os.write(r.getFileContent());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        return SUCCESS;
    }

    /**
     * executed when a user save or delete the filled form.
     * @return : delete, SUCCESS, INPUT
     * @throws Exception
     */
    public String save() throws Exception {

        log.debug("files:");
        for (File u: uploads) {
           log.debug("length:" + u + "\t" + u.length());
        }
        for (String n: uploadFileNames) {
           log.debug("uploadFileName:" + n);
        }
        for (String c: uploadContentTypes) {
           log.debug("uploadContentTypes:" + c);
        }

        boolean isNew = false;
        if(filledForm.getFfid() == null) {
            isNew=true;
        }

        if (delete != null) {
            return delete();
        }

        this.debugSubmit();
        this.updateFilledForm();
        this.cleanUpResponse();
        this.createResponse();

        String key ;
        if(isNew){
            key ="filledForm.added";
        }
        else{
            key="filledForm.updated";
        }
        saveMessage(getText(key));

        log.debug("filledForm:"+filledForm);
        log.debug("Template:"+filledForm.getTemplate());
        initDynamicFormParameters();
        if (isNew) {
            return SUCCESS;//created a new, then go to template List
        } else {
            return INPUT;//updated an existed one, keep staying on the page
        }
    }

    /**
     * update the data of filled form page to database.
     */
    private void updateFilledForm(){
        //fixed:when start a new filled form, an null exception thrown out.
        log.debug("this.ffid:"+this.ffid);//null
        log.debug("this.filledForm.getFfid:"+this.filledForm.getFfid());//null

//        this.filledForm=this.filledFormManager.get(this.filledForm.getFfid());

        this.filledForm.setUpdatedTime(new Date(System.currentTimeMillis()));
        User currentUser = null;
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx.getAuthentication() != null) {
            Authentication auth = ctx.getAuthentication();
            if (auth.getPrincipal() instanceof UserDetails) {
                currentUser = (User) auth.getPrincipal();
            } else if (auth.getDetails() instanceof UserDetails) {
                currentUser = (User) auth.getDetails();
            } else {
                throw new AccessDeniedException("User not properly authenticated.");
            }
        }
        this.filledForm.setUser(currentUser);
//        this.filledForm.setTemplate(this.templateManager.get(this.filledForm.getTemplate().getTid()));
        log.debug("Before saving filledForm.ffid:"+filledForm.getFfid());
        this.filledForm = this.filledFormManager.save(filledForm);//for getting the ffid
        log.debug("After  saving filledForm.ffid:"+filledForm.getFfid());
    }

    /**
     * remove those old related responses of the answers
     */
    private void cleanUpResponse(){
        Set<Response>responseSet = this.filledForm.getResponseSet();
        for (Response r : responseSet){
            this.responseManager.remove(r);// remove from database
        }
        this.filledForm.getResponseSet().clear();//remove from memory
    }

    /**
     * create the new response according user inputs
     */
    private void createResponse(){
//        Set<Response>responseSet = this.filledForm.getResponseSet();
        List<Response> responseList = this.analyseResponse();
        List<Response> savedList = new ArrayList<Response>();
        for(Response r: responseList ){
            savedList.add(this.responseManager.save(r));//add new response data to database
        }
        this.filledForm.getResponseSet().addAll(savedList);// associate response entities to filled form
    }

    /**
     * get user inputs from filled form page and create responses for them
     * @return: a list of responses of the filled form
     */
    private List<Response> analyseResponse(){
        List<Response> responseList = new ArrayList<Response>();
        Map<String,String[]> map = this.getRequest().getParameterMap();
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            String value = "";
            if (map.get(key).length==1){
                value = map.get(key)[0];
            }
            else if(map.get(key).length>1){
                log.error("Impossible value:"+Arrays.toString(map.get(key)));
            }
            log.debug("Answer key:"+key+" value:"+value);
            //FQMapping , AnswerType
            if (key.equals("FQMapping")){
                responseList.addAll(
                        this.buildResponse(
                                this.matchUploadedFiles(this.uploads,
                                        this.uploadFileNames,
                                        this.uploadContentTypes,
                                        value)));
            }
            else {
                String type =getTypeFromResponseKey(key);
                if(AnswerType.RADIO.toString().equals(type)){
                    String cid = getCombinedIdFromResponse(value);
                    responseList.add(
                        this.buildResponse(cid,"checked")
                    );
                }
                else if(AnswerType.CHECKBOX.toString().equals(type)){
                    String cid = getCombinedIdFromResponse(key);
                    responseList.add(
                        this.buildResponse(cid,"checked")
                    );
                }
                else if(AnswerType.TEXTAREA.toString().equals(type)){
                    String cid = getCombinedIdFromResponse(key);
                    responseList.add(
                        this.buildResponse(cid,value)
                    );
                }
                else if(AnswerType.TEXT.toString().equals(type)){
                    String cid = getCombinedIdFromResponse(key);
                    responseList.add(
                        this.buildResponse(cid,value)
                    );
                }
                else if(AnswerType.FILE.toString().equals(type)){
                    //do nothing
                }else{
                    log.debug("non-dynamic key:"+key);
                }
            }
        }
        return responseList;
    }


    private String getCombinedIdFromResponse(String responseValue){
        return responseValue.split("\\|")[1];
    }

    //get the type from combinedKey, e.g. CHECKBOX from CHECKBOX|Q_5-A_11
    private String getTypeFromResponseKey(String combinedKey){
//        String type = null;
        for (AnswerType at : AnswerType.values()){
            if (combinedKey.startsWith(at.toString())){
                return at.toString();
            }
        }
        return null;
    }

    /**
     * a overload method of buildResponse
     * build the responses of files uploaded by user.
     * @param uploadedFileList :  the file list uploaded by user.
     * @return: the response list of the information of uploaded files.
     */
    private List<Response> buildResponse(List<UploadedFile> uploadedFileList){
        List<Response> responseList = new ArrayList<Response>();
        for (UploadedFile uf : uploadedFileList){
            Response response = new Response();
            response.setFilledForm(this.filledForm);
            response.setQuestion(this.questionManager.get(this.getQid(uf.getCid())));
            response.setAnswer(this.answerManager.get(this.getAid(uf.getCid())));
            response.setFileType(uf.getFileType());
            response.setContent(uf.getName());
            try {

                FileInputStream fis=new FileInputStream(uf.file);
                int size = fis.available();
                log.debug("uploaded file size:"+size);
                int off = 0;
                byte[] allBytes = new byte[size];
                byte[] tempBytes = new byte[10240];
                int byteRead = 0;
                while ((byteRead = fis.read(tempBytes)) != -1) {
                    for (int i = 0; i < byteRead; i++) {
                        allBytes[off+i] = tempBytes[i];
                    }
                    off = off + byteRead;
                }
                response.setFileContent(allBytes);
                responseList.add(response);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseList;
    }
    //the output map's content is "combined id : cached File" such as "Q_2-A_3 : file"
    private List<UploadedFile> matchUploadedFiles(List<File> uploads,
                                                  List<String> uploadFileNames,
                                                  List<String> uploadContentTypes,
                                                  String FQMapping){
        List<UploadedFile> list= new ArrayList<UploadedFile>();

        log.debug("FQMapping:"+FQMapping);
        if (FQMapping==null||FQMapping.trim().length()==0){
            return list;
        }

        //analyse the FQMapping to a name:id map
        Map<String,String> nameMap= new HashMap<String,String>();
        String [] temp = FQMapping.split("\\*");//temp = Q_7-A_13|C:\fakepath\4.gif
        for (int i = 0; i < temp.length; i++) {
            if (temp[i]!=null||temp[i].length()!=0){

                String [] splited = temp[i].split("\\|");
                String cid = splited[0];
                String fullName = splited[1];
                int last = -1;
                if(fullName.lastIndexOf("\\")>=0){
                    last = fullName.lastIndexOf("\\");
                }
                if(fullName.lastIndexOf("/")>=0){
                    last = fullName.lastIndexOf("/");
                }
                log.debug("fullName:"+fullName);
                log.debug("last:"+last);
                last++;
                String name = fullName.substring(last);
                nameMap.put(name,cid);
            }
        }

        for (int i = 0; i < uploadFileNames.size(); i++) {
            String name = uploadFileNames.get(i);
            String combinedId = nameMap.get(name);
            UploadedFile uFile = new UploadedFile();
            uFile.setCid(combinedId);
            uFile.setName(name);
            uFile.setFile(uploads.get(i));
            uFile.setFileType(uploadContentTypes.get(i));
            list.add(uFile);
        }

        return list;
    }

    /**
     * a overload method of buildResponse
     * get the input from user on the filled page.
     * @param key : the key of parameter
     * @param value: the value of parameter
     * @return: a response object according to the key and value
     */
    private Response buildResponse(String key, String value){
        Response response = new Response();
        response.setFilledForm(this.filledForm);
        response.setQuestion(this.questionManager.get(this.getQid(key)));
        response.setAnswer(this.answerManager.get(this.getAid(key)));
        response.setContent(value);
        return response;
    }

    //the combined Id likes Q_5-A_9
    private Long getQid(String combinedId){
        String qPart = combinedId.split("-")[0];
        String qid = qPart.split("_")[1];
        return new Long(qid);
    }
    //the combined Id likes Q_5-A_9
    private Long getAid(String combinedId){
        String aPart = combinedId.split("-")[1];
        String aid = aPart.split("_")[1];
        return new Long(aid);
    }


    /**
     * a inner class for contain the information of files uploaded by user.
     */
    private class UploadedFile{
        String cid;
        String name;
        File file;
        String fileType;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }
    }

    /**
     * for the purpose of debugging
     */
    private void debugSubmit() {
        Map<String, String[]> map = this.getRequest().getParameterMap();
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); )
        {
            String key = it.next();
            String value = Arrays.toString(map.get(key));
            log.debug("Answer key:" + key + " value:" + value);
        }
    }

    /**
     * override the cancel method, as there two different direction for the
     * cancel button.
     * one is to go back to the page of template List
     * the other is to go back to the filled form list
     * @return
     */
    @Override
    public String cancel() {
        boolean isNew = (filledForm.getFfid() == null) ? true : false;
        log.debug("from:" + from);
        if (isNew) {
            return CANCEL + "_templates";// go back to template List
        } else {
            return CANCEL + "_filledForms";//go back to filled form list
        }
    }
}
