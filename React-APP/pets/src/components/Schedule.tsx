import React, {useState} from 'react';
import useForm from "react-hook-form";
import {connect} from "react-redux";
import {GlobalState} from "../App";
// @ts-ignore
import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import {Link} from "react-router-dom"
import Container from "react-bootstrap/Container";
import ListGroup from "react-bootstrap/ListGroup";
import Button from "react-bootstrap/Button";
import {fetchVetAppointments} from "../actions/ScheduleAction";
import '../main.css'

export interface Appointment {
    id: number,
    date: number,
    desc: string,
}

export interface AppointmentState {
    appointments: Appointment[],
    isFetching: boolean
}

type FormData = {
    petName: string;
    petSpecies: string;
}


const ProtoAppointmentList = (props: {
    state: GlobalState, appointments: Appointment[], isFetching: boolean, loadAppointment: (id: number)
        => void, postAppointment: (appointment: Appointment) => void, deleteAppointment: (id: number) => void
}) => {
    const [update, setUpdate] = React.useState(false);
    const [client, setClient] = useState({} as Client);
    const {register, setValue, handleSubmit, errors} = useForm<FormData>();
    const onSubmit = handleSubmit(({petName, petSpecies}) => {
        console.log(petName, petSpecies);
        //  props.postAppointment({name: petName, species: petSpecies, id: 0, ownerID: clientID});
        setUpdate(true);
    });

    let shoppingCart = [];


    // eslint-disable-next-line
    React.useEffect(() => {
        console.log("run effect");
        props.loadAppointment(5);

    }, []);

    let list = props.appointments.map((appointment: Appointment) => {
        return (
            <ListGroup.Item key={appointment.id}>
                <Link to={`/appointment/${appointment.id}/`}>{appointment.date.toString().substr(0,10)}</Link>
                <Button className="float-right" variant="primary" size="sm" onClick={() => {
                    props.deleteAppointment(appointment.id);
                    setUpdate(true)
                }}>Delete</Button>
            </ListGroup.Item>
        )
    });

    let emptyList = (<p className="text-center">You currently don't have any appointments!</p>)

    let petList = ((props.appointments.length > 0) ? <ListGroup>{list}</ListGroup> : emptyList)

    let calendario = props.appointments.map((appointment: Appointment) => {

            <ListGroup.Item key={appointment.id}>
                <Link to={`/appointment/${appointment.id}/`}>{appointment.date.toString().substr(0,10)}</Link>
                <Button className="float-right" variant="primary" size="sm" onClick={() => {
                    props.deleteAppointment(appointment.id);
                    setUpdate(true)
                }}>Delete</Button>
            </ListGroup.Item>

    });

    return (
        <Container>
            <br/>
            <h1 className="text-center">My Appointments</h1>
            <br/>

            {props.isFetching ? <p>Loading...</p> : petList}

            <br/>
            <h1 className="text-center">Appointments</h1>
            <div>


                <FullCalendar defaultView="dayGridMonth"
                              plugins={[dayGridPlugin]}
                              events={[
                                  {title: 'Appointement', date: props.appointments[0].date.toString().substr(0,10)},
                              ]}/>
            </div>

        </Container>
    );
};

export interface Client {
    id: number,
    name: string,
    username: string
}

const mapStateToProps = (state: GlobalState) => ({
    pets: state.pets.pets,
    currentUser: state.signIn.currentUser,
    currentRole: state.signIn.currentRole,
    appointments: state.appointments.appointments
});


const mapDispatchToProps = (dispatch: any) => {
    return {
        /*   postAppointment: (appointment: Appointment) => {
               dispatch(fetchAppointment())
           },*/
        loadAppointment: (id: number) => {
            dispatch(fetchVetAppointments(id))
        },
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(ProtoAppointmentList);