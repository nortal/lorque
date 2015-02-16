package com.nortal.lorque.plugin.rengy.job;

//@Path("jobs")
public class ReportDownloadResource {

//  @GET
//  @Path("{jobId}/report")
//  public Response downloadReport(@PathParam("jobId") Long jobId) {
//    // Job job = jobService.getJob(jobId);
//    // jobService.getReport(jobId);
//    final byte[] content = new byte[]{};
//
//    if (content == null) {
//      throw new WebApplicationException("Job output not found.", Response.Status.NOT_FOUND);
//    }
//
//    StreamingOutput streamingOutput = new StreamingOutput() {
//      @Override
//      public void write(OutputStream os) throws IOException, WebApplicationException {
//        try {
//          os.write(content);
//          os.flush();
//        } catch (IOException e) {
//          throw new WebApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
//        }
//      }
//    };
//
//    Response.ResponseBuilder builder = Response.status(Response.Status.OK);
//    return builder.type(getMediaType("foo.pdf")).entity(streamingOutput).build();
//
//  }
//
//  private String getMediaType(String filename) {
//    if (filename.toLowerCase().endsWith(".pdf")) {
//      return "application/pdf";
//    }
//    return MediaType.APPLICATION_OCTET_STREAM;
//  }

}
