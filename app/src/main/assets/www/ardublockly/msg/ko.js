var Ardublockly = Ardublockly || {};
Ardublockly.LOCALISED_TEXT = {
  translationLanguage: "한국어",
  title: "Arduroid",
  blocks: "블록들",
  /* Menu */
  open: "Open",
  save: "Save",
  deleteAll: "Delete All",
  settings: "설정",
  documentation: "Documentation",
  reportBug: "도움말",
  examples: "예제",
  /* Settings */
  compilerLocation: "Compiler Location",
  compilerLocationDefault: "Compiler Location unknown",
  sketchFolder: "Sketch Folder",
  sketchFolderDefault: "Sketch Folder unknown",
  arduinoBoard: "아두이노 보드",
  arduinoBoardDefault: "아두이노 보드를 찾을 수 없습니다.",
  comPort: "COM Port",
  comPortDefault: "COM Port unknown",
  defaultIdeButton: "컴파일 옵션",
  defaultIdeButtonDefault: "컴파일 옵션이 지정되지 않았습니다.",
  language: "언어",
  languageDefault: "언어를 찾을 수 없습니다.",
  sketchName: "Sketch Name",
  /* Arduino console output */
  arduinoOpMainTitle: "아두이노 IDE 결과창",
  arduinoOpWaiting: "IDE의 응답을 기다리는 중...",
  arduinoOpUploadedTitle: "성공적으로 업로드가 완료되었습니다.",
  arduinoOpVerifiedTitle: "스케치 검사가를 성공적으로 완료하였습니다.",
  arduinoOpOpenedTitle: "Sketch opened in IDE",
  arduinoOpOpenedBody: "The sketch should be loaded in the Arduino IDE.",
  arduinoOpErrorTitle: "에러가 발생하였습니다.",
  arduinoOpErrorIdContext_0: "에러가 없습니다.",
  arduinoOpErrorIdContext_1: "스케치 검사 혹은 업로드 중에 문제가 발생했습니다.",
  arduinoOpErrorIdContext_2: "스케치를 찾을 수 없습니다.",
  arduinoOpErrorIdContext_3: "잘못된 명령어 입력입니다.",
  arduinoOpErrorIdContext_4: "Preference passed to 'get-pref' flag does not exist.",
  arduinoOpErrorIdContext_5: "아두이노 IDE 에러, 다시 시도하십시오.",
  arduinoOpErrorIdContext_50: "아두이노 IDE 에러, 다시 시도하십시오.",
  arduinoOpErrorIdContext_51: "스케치를 생성할 수 없습니다.",
  arduinoOpErrorIdContext_52: "Invalid path to internally created sketch file",
  arduinoOpErrorIdContext_53: "Unable to find Arduino IDE<br>" +
                              "The compiler directory has not been set correctly.<br>" +
                              "Please ensure the path is correct in the Settings.",
  arduinoOpErrorIdContext_54: "What should we do with the Sketch?<br>" +
                              "The launch IDE option has not been set.<br>" +
                              "Please select an IDE option in the Settings.",
  arduinoOpErrorIdContext_55: "비정상적인 시리얼 포트 연결<br>" +
                              "시리얼 포트를 찾을 수 없습니다.<br>" +
                              "아두이노 보드가 정상적으로 연결되어 인식되는지 확인해 주세요.",
  arduinoOpErrorIdContext_56: "아두이노 보드 인식 문제 발생<br>" +
                              "아두이노 보드가 정상적으로 연결되어 있지 않습니다. <br>" +
                              "아두이노 보드 연결을 확인해 주십시오.",
  arduinoOpErrorIdContext_52: "예상치 못한 서버의 오류",
  arduinoOpErrorIdContext_64: "연결 중에 문제가 발생했습니다.",
  arduinoOpErrorUnknown: "예상치 못한 에러",
  /* Modals */
  noServerTitle: "아두로이드의 서버가 작동하지 않습니다.",
  noServerTitleBody: "<p>아두로이드의 서버에 현재 문제가 발생했습니다.</p>" +
                     "<p>빠른 시일 내에 정상작동 하도록 하겠습니다. </p>" +
                     "<p>이용에 불편을 드려서 정말 죄송합니다. </p>",
  noServerNoLangBody: "If the Ardublockly application is not running the language cannot be fully changed.",
  addBlocksTitle: "Additional Blocks",
  /* Alerts */
  loadNewBlocksTitle: "새 블록들을 불러올까요?",
  loadNewBlocksBody: "불러오는 XML 파일이 현재 작업창을 대체할 것입니다. <br>" +
                     "정말 교체하시겠습니까?",
  discardBlocksTitle: "작업창 초기화?",
  discardBlocksBody: "%1 개의 블록이 배치되어 있습니다.<br>" +
                     "정말 작업화면을 초기화 하시겠습니까?",
  invalidXmlTitle: "잘못된 형식의 XML",
  invalidXmlBody: "XML 파일을 불러오는데 실패했습니다. 다시 시도하시거나 올바른 파일을 선택해 주세요.",
  /* Tooltips */
  uploadingSketch: "스케치를 아두이노에 업로딩...",
  uploadSketch: "아두이노에 스케치 업로드",
  verifyingSketch: "스케치 검사중...",
  verifySketch: "스케치 검사",
  openingSketch: "Opening Sketch in the Arduino IDE...",
  openSketch: "Open Sketch in IDE",
  notImplemented: "아직 구현되지 않은 함수입니다.",
  /* Prompts */
  ok: "OK",
  okay: "네",
  cancel: "아니요",
  return: "돌아가기",
  /* Cards */
  arduinoSourceCode: "Arduino Source Code",
  blocksXml: "Blocks XML",
  /* Toolbox Categories*/
  catLogic: "논리",
    catLoops: "반복",
    catMath: "연산",
    catText: "텍스트",
    catVariables: "변수",
    catFunctions: "함수",
    catInputOutput: "입/출력",
    catTime: "시간",
    catAudio: "소리",
    catMotors: "모터",
    catComms: "통신",
};
