import { useLocation } from "react-router-dom"
import { FaEdit } from "react-icons/fa";
import {  FaCheck } from "react-icons/fa6";
import { MdDelete } from "react-icons/md";
import { MdClose } from "react-icons/md";
import { IoClose } from "react-icons/io5";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";


export default function Class() {
  const location = useLocation();
  const navigate = useNavigate();
  const {classroom} = location.state || {classroom: null}
  const [studentId, setStudentId] = useState('');
  const [studentName, setStudentName] = useState('');
  const [studentAge, setStudentAge] = useState(0);
  const [studentGrade, setStudentGrade] = useState(0);
  const [displayStudentForm, setDisplayStudentForm] = useState(false);
  const [addStudentError, setAddStudentError] = useState('');
  const [classRoomData, setClassRoomData] = useState(classroom);
  const [showIcons, setShowIcons] = useState(null);
  const [displayChangeNameForm, setDisplayChangeNameForm] = useState(false);
  const [displayChangeDescriptionForm, setDisplayChangeDescriptionForm] = useState(false);
  const [newName, setNewName] = useState('');
  const [newDescription, setNewDescription] = useState('');
  const [displayDeleteClassConfirm, setDisplayDeleteClassConfirm] = useState(false);
  const [displayEditStudentForm, setDisplayEditStudentForm] = useState(false);
  const [displayDeleteStudentConfirm, setDisplayDeleteStudentConfirm] = useState(false);
  const [newStudentName, setNewStudentName] = useState('');
  const [newStudentAge, setNewStudentAge] = useState(0);
  const [newStudentGrade, setNewStudentGrade] = useState(0);
  const [characterCount, setCharacterCount] = useState(0);
  const [classAvg, setClassAvg] = useState(0);

  const {user} = useContext(AuthContext);

  const handleAddStudent = (e) => {
    e.preventDefault();
    const apiUrl = `http://localhost:8081/api/students/${user.user.userId}/${classroom?.classId}/add-student`
    axios.post(apiUrl, {
      'studentId': studentId,
      'name': studentName,
      'age': studentAge,
      'grade': studentGrade
    },{
      headers: {
        Authorization: `Bearer ${user.jwt}`
      }
    }).then(response => {
      setDisplayStudentForm(false);
      fetchUpdatedClassroom();
    }).catch(error => {
      setAddStudentError(error.response.data)
    })

  }

  const handleNameChange = (e) => {
    e.preventDefault();
    const apiUrl = `http://localhost:8081/api/classroom/${user.user.userId}/${classroom?.classId}`
    axios.patch(apiUrl, {
      'name': newName
    },{
      headers: {
        Authorization: `Bearer ${user.jwt}`
      }
    }).then(response => {
      setDisplayChangeNameForm(false);
      fetchUpdatedClassroom();
    })
  }

  const handleDescriptionChange = (e) => {
    setCharacterCount(e.target.value.length);
    setNewDescription(e.target.value.trimStart());
  }

  const handleDescriptionChangeSubmit = (e) => {
    e.preventDefault();
     const apiUrl = `http://localhost:8081/api/classroom/${user.user.userId}/${classroom?.classId}`
    axios.patch(apiUrl, {
      'description': newDescription
    },{
      headers: {
        Authorization: `Bearer ${user.jwt}`
      }
    }).then(response => {
      setDisplayChangeDescriptionForm(false);
      fetchUpdatedClassroom();
    })
  }

  const handleDeleteClass = (e) => {
    e.preventDefault();
    const apiUrl = `http://localhost:8081/api/classroom/${user.user.userId}/${classroom?.classId}`
    axios.delete(apiUrl,{
      headers: {
        Authorization: `Bearer ${user.jwt}`
      }
    }).then(response => {
      setDisplayDeleteClassConfirm(false);
      navigate('/');
    }).catch(error => {
      console.error('Error deleting classroom:', error);
    })
  }

  const handleEditStudentSubmit = (e) => {
    e.preventDefault();
    const dataToUpdate = {};
    if (newStudentName !== null && newStudentName !== studentName) {
      dataToUpdate.name = newStudentName
    }
    if (newStudentAge > 0 && newStudentAge !== studentAge) {
      dataToUpdate.age = newStudentAge
    }
    if (newStudentGrade > 0 && newStudentGrade !== studentGrade) {
      dataToUpdate.grade = newStudentGrade
    }
    const apiUrl = `http://localhost:8081/api/students/${user.user.userId}/${classroom?.classId}/${studentId}/update-student`
    axios.patch(apiUrl, dataToUpdate, {
      headers: {
        Authorization: `Bearer ${user.jwt}`
      }
    }).then(response => {
      setDisplayEditStudentForm(false);
      fetchUpdatedClassroom();
    }).catch(error => {
      console.error(error);
    })
  }

  const handleStudentDelete = (e) => {
    e.preventDefault();
    const apiUrl = `http://localhost:8081/api/students/${user.user.userId}/${classroom?.classId}/${studentId}/delete-student`
    axios.delete(apiUrl, {
      headers: {
        Authorization: `Bearer ${user.jwt}`
      }
    }).then(response => {
      setDisplayDeleteStudentConfirm(false);
      fetchUpdatedClassroom();
    }).catch(error => {
      console.error(error);
    })
    
  }

  const fetchUpdatedClassroom = () => {
    const apiUrl = `http://localhost:8081/api/classroom/${user.user.userId}/${classroom?.classId}`
    axios.get(apiUrl, {
      headers: {
        Authorization: `Bearer ${user.jwt}`
      }
    }).then(response => {
      setClassRoomData(response.data);
      console.log(response.data);
    }).catch(error => {
      console.error(error);
    })
  }
  useEffect(() => {
    fetchUpdatedClassroom();
  }, [])

  return (
    <>
    {displayDeleteStudentConfirm && (
      <main className="fixed top-0 left-0 flex items-center justify-center bg-black bg-opacity-75 w-full h-full z-20 overflow-auto">
        <div className="flex flex-col justify-between items-center bg-white p-4 rounded-lg h-1/5 w-1/5 min-h-[100px] min-w-[100px]">
          <div>
            Delete student: {studentId}?
          </div>
          <div className="flex w-full gap-1">
            <div className="flex border-2 rounded-lg border-teal-300 hover:bg-teal-300 text-teal-300 hover:text-white transition:hover duration-300 w-3/6 justify-center items-center text-sm cursor-pointer" onClick={handleStudentDelete}>
              <FaCheck/>
            </div>
            <div className=" flex border-2 rounded-lg border-red-500 hover:bg-red-500 text-red-500 hover:text-white transition:hover duration-300 w-3/6 justify-center items-center cursor-pointer" onClick={() => setDisplayDeleteStudentConfirm(false)}>
              <IoClose/>
            </div>
          </div>
        </div>

        <button className='absolute top-1 right-1 text-red-500 text-3xl' onClick={(e) => setDisplayDeleteStudentConfirm(false)}><IoClose/></button>
      </main>
    )}
    {displayEditStudentForm && (
      <main className="fixed top-0 left-0 flex items-center justify-center  bg-black bg-opacity-75 w-full h-full z-20 overflow-auto">
        <form onSubmit={handleEditStudentSubmit} className='relative bg-white p-5 rounded-lg h-auto w-2/6 max-w-[400px] min-w-[250px] min-h-[250px]'>
          <label>Student ID:</label>
          <div className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 w-full h-auto'>
            {studentId}
          </div>

          <input type="text" placeholder={studentName} className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 w-full' maxLength="30" onChange={(e) => setNewStudentName(e.target.value.trimStart())}/>

          <label htmlFor="student_age">Student age:</label>
          <input
          id="student_age"
          type="number"
          value={newStudentAge}
          onChange={(e) => setNewStudentAge(e.target.value)}
          placeholder={newStudentAge}
          onInput={(e) => {
            e.target.value = !!e.target.value && Math.abs(e.target.value) >= 0 ? Math.abs(e.target.value) : null;
          }}
          className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 w-full'/>
          
          <label htmlFor="student_grade">Student grade:</label>
          <input
          id="student_grade"
          type="number"
          value={newStudentGrade}
          onChange={(e) => setNewStudentGrade(e.target.value)}
          placeholder={newStudentGrade}
          onInput={(e) => {
            e.target.value = !!e.target.value && Math.abs(e.target.value) >= 0 ? Math.abs(e.target.value) : null;
          }}
          className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-6 w-full'/>

          <button type="submit" className=' absolute bottom-2 right-2 h-auto pl-1 pr-1 border-2 rounded-lg border-teal-300 hover:bg-teal-300 transition:hover duration-300' disabled={!newStudentName && !newStudentAge && !newStudentGrade || (newStudentName === studentName && newStudentAge === studentAge && newStudentGrade === studentGrade)}>Update student</button>
        </form>
        <button className='absolute top-1 right-1 text-red-500 text-3xl' onClick={(e) => setDisplayEditStudentForm(false)}><IoClose/></button>
      </main>
    )}

    {displayDeleteClassConfirm && (
      <main className="fixed top-0 left-0 flex items-center justify-center bg-black bg-opacity-75 w-full h-full z-20 overflow-auto">
        <div className="flex flex-col justify-between items-center bg-white p-4 rounded-lg h-1/5 w-1/5 min-h-[100px] min-w-[100px]">
          <div>
            Are you sure?
          </div>
          <div className="flex w-full gap-1">
            <div className="flex border-2 rounded-lg border-teal-300 hover:bg-teal-300 text-teal-300 hover:text-white transition:hover duration-300 w-3/6 justify-center items-center text-sm cursor-pointer" onClick={handleDeleteClass}>
              <FaCheck/>
            </div>
            <div className=" flex border-2 rounded-lg border-red-500 hover:bg-red-500 text-red-500 hover:text-white transition:hover duration-300 w-3/6 justify-center items-center cursor-pointer" onClick={() => setDisplayDeleteClassConfirm(false)}>
              <IoClose/>
            </div>
          </div>
        </div>

        <button className='absolute top-1 right-1 text-red-500 text-3xl' onClick={(e) => setDisplayDeleteClassConfirm(false)}><IoClose/></button>
      </main>
    )}

    {displayChangeNameForm && (
      <main className="fixed top-0 left-0 flex items-center justify-center bg-black bg-opacity-75 w-full h-full z-20 overflow-auto">
        <form action="submit" onSubmit={handleNameChange} className='relative bg-white p-5 rounded-lg h-auto w-2/6 min-w-[250px] min-h-[100px]'>
          <input type="text" placeholder="New class name..." className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 w-full' maxLength="30" onChange={(e) => setNewName(e.target.value.trimStart())}/>
          <button type="submit" className=' absolute bottom-2 right-2 h-auto pl-1 pr-1 border-2 rounded-lg border-teal-300 hover:bg-teal-300 transition:hover duration-300' disabled={!newName | newName === classroom?.name | newName === classRoomData?.name}>Confirm</button>
        </form>
        <button className='absolute top-1 right-1 text-red-500 text-3xl' onClick={(e) => setDisplayChangeNameForm(false)}><IoClose/></button>
      </main>
    )}

    {displayChangeDescriptionForm && (
      <main className="fixed top-0 left-0 flex items-center justify-center bg-black bg-opacity-75 w-full h-full z-20 overflow-auto">
      <form action="submit" onSubmit={handleDescriptionChangeSubmit} className='relative bg-white p-5 rounded-lg h-auto w-2/6 min-w-[250px] min-h-[170px] max-w-[600px]'>
        <textarea type="text" placeholder="New description..." className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-4 w-full break-words h-auto min-h-[130px]' style={{resize:'none'}} onChange={handleDescriptionChange}/>
        <p className={`-mt-2 mb-4 text-xs ${characterCount <= 300 ? 'text-black': 'text-red-500'}`}>{characterCount}/300 {characterCount > 300 ? <span className='text-xs text-red-500'>Cannot exceed character count</span>: ''}</p>
        
        <button type="submit" className=' absolute bottom-2 right-2 h-auto pl-1 pr-1 border-2 rounded-lg border-teal-300 hover:bg-teal-300 transition:hover duration-300' disabled={!newDescription | newDescription === classroom?.description | newDescription === classRoomData?.description | characterCount > 300}>Confirm</button>
      </form>
      <button className='absolute top-1 right-1 text-red-500 text-3xl' onClick={(e) => setDisplayChangeDescriptionForm(false)}><IoClose/></button>
    </main>
    )}
    {displayStudentForm && (
      <main className="fixed top-0 left-0 flex items-center justify-center bg-black bg-opacity-75 w-full h-full z-20 overflow-auto">
        <form action="submit" onSubmit={handleAddStudent} className='relative bg-white p-5 rounded-lg h-auto w-2/6 min-w-[250px] min-h-[300px] max-w-[450px]'>
          <input type="text" placeholder='Student ID...' required value={studentId} onChange={(e) => setStudentId(e.target.value.trim())} className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 w-full'/>

          <input type="text" placeholder='Student name...' required value={studentName} onChange={(e) => setStudentName(e.target.value.trimStart())} className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 w-full'/>

          <label htmlFor="student_age">Student age:</label>
          <input
          id="student_age"
          type="number"
          required
          value={studentAge}
          onChange={(e) => setStudentAge(e.target.value)}
          min="1"
          onInput={(e) => {
            e.target.value = !!e.target.value && Math.abs(e.target.value) >= 0 ? Math.abs(e.target.value) : null;
          }}
          className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 w-full'/>
          
          <label htmlFor="student_grade">Student grade:</label>
          <input
          id="student_grade"
          type="number"
          value={studentGrade}
          onChange={(e) => setStudentGrade(e.target.value)}
          min="1"
          onInput={(e) => {
            e.target.value = !!e.target.value && Math.abs(e.target.value) >= 0 ? Math.abs(e.target.value) : null;
          }}
          className='border-2 rounded-lg border-teal-300 outline-none p-1 mr-2 mb-2 w-full'/>

          <p className='left-4 text-xs  text-red-500 '>{addStudentError}</p>

          <button type="submit" className=' absolute bottom-2 right-2 h-auto pl-1 pr-1 border-2 rounded-lg border-teal-300 hover:bg-teal-300 transition:hover duration-300' disabled={!studentId | !studentName| !studentAge}>Add student</button>
        </form>
         <button className='absolute top-1 right-1 text-red-500 text-3xl' onClick={(e) => setDisplayStudentForm(false)}><IoClose/></button>
      </main>
    )}
    <div className=" h-lvh w-lvw flex flex-col items-center bg-slate-950 overflow-auto">
      <div className=" pt-20 pb-10">
        <h1 className="text-3xl text-teal-400">{classroom?.classId}</h1>
      </div>
       <div className="bg-white flex flex-col justify-between gap-2  h-auto w-8/12 min-w-[250px] max-w-[600px] p-3 border-2 rounded-lg border-teal-400">
        <div className="flex flex-col gap-2">
          <div className="relative">
            <label>Class name:</label>
              <div className=" w-full h-8 border-2 border-teal-400 rounded-lg flex justify-between items-center p-1">
              <div>{classRoomData?.name}</div>
              <div className="absolute top-1 right-0 text-teal-400 cursor-pointer" onClick={(e) => setDisplayChangeNameForm(true)}><FaEdit/></div>
            </div>
          </div>

          <div className="relative">
            <label>Class description:</label>
            <div className="w-full h-auto max-h-[100px] border-2 border-teal-400 rounded-lg p-1 overflow-auto">
              <div>{classRoomData?.description}</div>
            </div>
            <div className="absolute top-1 right-1 text-teal-400 cursor-pointer" onClick={(e) => setDisplayChangeDescriptionForm(true)}><FaEdit/></div>
          </div>
          
          <div className="flex justify-between items-center -mb-2">
            <div className="flex gap-1 items-center">
              <label>Class size: </label>
              <div className="w-auto h-auto min-h-7  border-2 border-teal-400 rounded-lg px-1">{classRoomData?.students.length}</div>
            </div>

            <div className="flex gap-1 items-center">
                <label>Class Average: </label>
                <div className="w-auto border-2 border-teal-400 rounded-lg px-1">{Math.round(classRoomData?.classAvg)}</div>
            </div>
          </div>
              
          <div className="h-auto">
            <label>Students:</label>
            <div className="items-center w-full h-auto min-h-12 max-h-[200px] overflow-auto border-2 p-1 border-teal-400 rounded-lg mb-2">
              <ul className="flex flex-col gap-2 w-8/12 mr-1">
               {classRoomData?.students.map((student) => {
                return (
                  <li key={student.studentId} className="flex justify-between items-center border-2 border-teal-400 rounded-lg w-full p-1" onMouseEnter={() => setShowIcons(student.studentId)} onMouseLeave={() => setShowIcons(null)}>
                    <p>
                      {student?.name}
                    </p>
                    {showIcons === student.studentId && (
                      <div className="flex gap-1">
                      <FaEdit className="text-teal-400 cursor-pointer" onClick={() => {setDisplayEditStudentForm(true); setStudentId(student.studentId); setStudentName(student.name); setStudentAge(student.age); setStudentGrade(student.grade); setNewStudentAge(student.age); setNewStudentGrade(student.grade)}}/>
                      <MdDelete onClick={() => {setStudentId(student.studentId); setDisplayDeleteStudentConfirm(true)}} className="text-red-500 cursor-pointer"/>
                    </div>
                    )}     
                </li>
                )
               })} 
              </ul> 
            </div>
            <button className='h-7 pl-1 pr-1 w-full border-2 rounded-lg border-teal-300 hover:bg-teal-300 transition:hover duration-300 text-xs hover:text-white' onClick={() => {setDisplayStudentForm(true); setStudentId(''); setStudentName(''); setStudentAge(0); setStudentGrade(0); setAddStudentError('')}}>Add a student</button>
            
          </div>
        </div>
          
          
          <div className="flex flex-col gap-2">
            <button className=' bottom-2 h-7 pl-1 pr-1 border-2 rounded-lg border-teal-300 hover:bg-teal-300 transition:hover duration-300 hover:text-white' onClick={() => navigate('/')}>Return Home</button>
          <button className=' bottom-2 h-7 pl-1 pr-1 border-2 rounded-lg border-red-500 hover:bg-red-500 transition:hover duration-300 hover:text-white' onClick={() => setDisplayDeleteClassConfirm(true)}>Delete Class</button>
          </div>
        </div> 
       </div>
    </>
    
  )
}
