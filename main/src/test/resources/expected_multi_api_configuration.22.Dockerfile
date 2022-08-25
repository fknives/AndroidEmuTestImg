FROM fknives2/android-test-img-multi:1.0.1
# Generated on 2016-05-28

ENV EMULATOR_API_LEVEL 22

RUN /usr/local/bin/androidemulatorstart --buildOnly

CMD ["/bin/bash"]