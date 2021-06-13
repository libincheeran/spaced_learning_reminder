import OverdueList from './components/LcProblemOverdue';
import LcProblemAdd from './components/LcProblemAdd';
import React from 'react';

function App() {

  const [toggle, setToggleRender] = React.useState(false);

  return (
    <div>
      <LcProblemAdd toggle={toggle} setRender={setToggleRender}></LcProblemAdd>
      <br></br>
      <br></br>
      <OverdueList toggle={toggle} setRender={setToggleRender}></OverdueList>
    </div>
  );
}

export default App;
