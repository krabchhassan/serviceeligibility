package com.cegedim.next.serviceeligibility.extractrecipient.services;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5Recipient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.IOException;

public class FileService {
  private final BufferedWriter writer;

  private String buffer;

  public FileService(BufferedWriter writer) {
    this.writer = writer;
    this.buffer = "";
  }

  public void addContractToFile(ContratAIV5Recipient servicePrestation) throws IOException {
    writeInBuffer(new ObjectMapper().writeValueAsString(servicePrestation));
  }

  public void writeFileStart() {
    writeInBuffer("[");
  }

  public void writeComma() {
    writeInBuffer(",");
  }

  public void writeFileEnd() {
    writeInBuffer("]");
  }

  private void writeInBuffer(String content) {
    this.buffer += content;
  }

  public void writeInFile() throws IOException {
    writer.write(this.buffer);
    this.buffer = "";
  }
}
