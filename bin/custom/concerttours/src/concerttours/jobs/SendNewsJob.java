package concerttours.jobs;

import concerttours.model.NewsModel;
import concerttours.service.NewsService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.mail.MailUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

public class SendNewsJob extends AbstractJobPerformable<CronJobModel> {

    private static final Logger LOG = Logger.getLogger(SendNewsJob.class);
    private NewsService newsService;
    private ConfigurationService configurationService;

    @Override
    public PerformResult perform(CronJobModel cronJobModel) {

        LOG.info("Sending news mails. Note that org.apache.commons.mail.send() can block if behind a firewall/proxy.");
        final List<NewsModel> news = getNewsService().getNewsOfTheDay(new Date());
        if(news.isEmpty()){
            LOG.info("No news items for today, skipping send of ranking mails");
            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        }
        final StringBuilder mailContentBuilder = new StringBuilder(2000);
        int index = 1;
        mailContentBuilder.append("Noticias de hoje:\n\n");
        for(final NewsModel newsModel : news){
            mailContentBuilder.append(index++);
            mailContentBuilder.append(". ");
            mailContentBuilder.append(newsModel.getHeadline());
            mailContentBuilder.append("\n");
            mailContentBuilder.append(newsModel.getContent());
            mailContentBuilder.append("\n\n");
        }
        try{
            sendMail(mailContentBuilder.toString());
        }catch(EmailException e){
            LOG.error("Problem sending new email. Note that org.apache.commons.mail.send() can block if behind a firewall/proxy.)");
            LOG.error("Problem sending new email.", e);
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    private void sendMail(final String message) throws EmailException {
        final String subject = "Noticias do dia";
        //Configuração do servidor de email
        final Email email = MailUtils.getPreConfiguredEmail();
        //Enviando mensagem
        Configuration config = getConfigurationService().getConfiguration();
        String recipient = config.getString("news_summary_mailing_address", null);
        email.addTo(recipient);
        email.setSubject(subject);
        email.setMsg(message);
        email.setTLS(true);
        email.send();
        LOG.info(subject);
        LOG.info(message);
    }

    @Required
    public NewsService getNewsService() { return newsService; }
    @Required
    public void setNewsService(NewsService newsService) { this.newsService = newsService; }
    @Required
    public ConfigurationService getConfigurationService() { return configurationService; }
    @Required
    public void setConfigurationService(ConfigurationService configurationService) { this.configurationService = configurationService; }
}
