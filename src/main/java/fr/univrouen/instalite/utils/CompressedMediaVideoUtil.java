package fr.univrouen.instalite.utils;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class CompressedMediaVideoUtil {

//    private FFmpeg ffmpeg;
//    private FFprobe ffprobe;
//
//    @Value("${app.upload.dir}")
//    private String uploadDir;
//
//    public CompressedMediaVideoUtil(
//            @Value("${server.compression.ffmpeg}") String ffmpegPath,
//            @Value("${server.compression.ffprobe}") String ffprobePath) throws IOException {
//        ffmpeg = new FFmpeg();
//        if (!StringUtils.isEmpty(ffmpegPath))
//            ffmpeg = new FFmpeg(ffmpegPath);
//
//        ffprobe = new FFprobe();
//        if (!StringUtils.isEmpty(ffprobePath))
//            ffprobe = new FFprobe(ffprobePath);
//
//    }
//
//    public String generateThumbnail(String fileName) throws IOException {
//        String dirPath = uploadDir + File.separator +
//                "Videos" + File.separator +
//                "thumbnail" + File.separator +
//                LocalDate.now();
//
//        File dir = new File(dirPath);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        String outputLocation = dirPath +
//                File.separator + UUID.randomUUID().toString() + "." + "png";
//        FFmpegBuilder builder = new FFmpegBuilder();
//        FFmpegProbeResult input = ffprobe.probe(fileName);
//        builder.setInput(input)
//                .addOutput(outputLocation)
//                .setFrames(1)
//                .setVideoFilter("select='gte(n\\,10)',scale=200:-1")
//                .done();
//
//        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
//        executor.createJob(builder).run();
//        return outputLocation;
//    }
//
//    public String convertVideo(String fileName, String format) throws IOException {
//        String dirPath = uploadDir + File.separator +
//                "Videos" + File.separator +
//                "compressed" + File.separator +
//                LocalDate.now();
//
//        File dir = new File(dirPath);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        String outputLocation = dirPath +
//                File.separator + UUID.randomUUID().toString() + "." + format;
//        FFmpegProbeResult input = ffprobe.probe(fileName);
//        FFmpegBuilder builder = new FFmpegBuilder()
//                .setInput(input).overrideOutputFiles(true)
//
//                .addOutput(outputLocation)
//                .setFormat(format)
//                .disableSubtitle()
//
////                config audio
//                .setAudioChannels(1)
//                .setAudioCodec("aac")
//                .setAudioSampleRate(48_000)
//                .setAudioBitRate(32_768)
//
////                config video
//                .setVideoCodec("libx264")
//                .setVideoFrameRate(24, 1)
//                .setVideoResolution(320, 240)
//
//                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
//                .done();
//
//        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
//        executor.createJob(builder, new ProgressListener() {
//
//            final double duration_ns = input.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
//
//            @Override
//            public void progress(Progress progress) {
//                double percentage = progress.out_time_ns / duration_ns;
//
//                if (progress.status.equals(Progress.Status.END)) {
//                    System.out.println("filename: " + input.getFormat().filename + " completed!");
//                } else {
//                    System.out.printf("filename: %s -> [%.0f%%] status: %s time: %s%n",
//                            input.getFormat().filename,
//                            percentage * 100,
//                            progress.status,
//                            FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS)
//                    );
//                }
//            }
//        }).run();
//        return outputLocation;
//    }


}

